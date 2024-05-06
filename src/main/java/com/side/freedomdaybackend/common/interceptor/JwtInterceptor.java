package com.side.freedomdaybackend.common.interceptor;

import com.side.freedomdaybackend.common.config.RedisConfig;
import com.side.freedomdaybackend.common.constants.Constants;
import com.side.freedomdaybackend.common.exception.CustomException;
import com.side.freedomdaybackend.common.exception.ErrorCode;
import com.side.freedomdaybackend.common.util.CookieUtil;
import com.side.freedomdaybackend.common.util.EncryptUtil;
import com.side.freedomdaybackend.common.util.JwtUtil;
import com.side.freedomdaybackend.common.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;
//    private final EncryptUtil encryptUtil;

//    private final AuthTokenRepository authTokenRepository;

//    private final AuthTokenMapper authTokenMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = null;
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        if(request.getMethod().equals("OPTIONS")) return true;
        if(cookies == null) throw new CustomException(ErrorCode.JWT_ERROR);
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(Constants.ACCESS_TOKEN)) accessToken = cookie.getValue();
            if (cookie.getName().equals(Constants.REFRESH_TOKEN)) refreshToken = cookie.getValue();
        }

        try {
            // accessToken 확인
            jwtUtil.isValidToken(accessToken);
        } catch (ExpiredJwtException e) {
            // refreshToken 확인
            Claims claims = refreshTokenCheck(refreshToken, response);
            String uuid = (String) claims.get(Constants.UUID);
            String memberId = (String) claims.get(Constants.MEMBER_ID);

            // redis refreshToken 존재 확인
            if (redisUtil.notExists(uuid)) {
                logout(response);
                throw new CustomException(ErrorCode.JWT_REFRESH_NOT_FOUND);
            }

            // 재발급 시작
            String newAccessToken = jwtUtil.createAccessToken(memberId);
            ResponseCookie accessCookie = cookieUtil.createToken(Constants.ACCESS_TOKEN, newAccessToken);
            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Constants.ACCESS_TOKEN)) cookie.setValue(accessCookie.toString().replaceAll("Access-Token=", "").substring(0, accessCookie.toString().replaceAll("Access-Token=", "").indexOf(";")));
            }

            return HandlerInterceptor.super.preHandle(request, response, handler);
        } catch (SignatureException e) {
            throw new CustomException(ErrorCode.JWT_ACCESS_INVALID_SIGNATURE);
        } catch (JwtException e) {
            logout(response);
            throw new CustomException(ErrorCode.JWT_ERROR);
        } catch (Exception e) {
            logout(response);
            throw new CustomException(ErrorCode.JWT_ERROR);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private Claims refreshTokenCheck(String refreshToken, HttpServletResponse response) {
        Claims refreshClaims = null;

        try {
            refreshClaims = jwtUtil.isValidToken(refreshToken);
        } catch (ExpiredJwtException e) {
            logout(response);
            throw new CustomException(ErrorCode.JWT_REFRESH_EXPIRE);
        } catch (SignatureException e) {
            logout(response);
            throw new CustomException(ErrorCode.JWT_REFRESH_INVALID_SIGNATURE);
        } catch (Exception e) {
            logout(response);
            throw new CustomException(ErrorCode.JWT_ERROR);
        }

        return refreshClaims;
    }

    private void logout(HttpServletResponse response) {
        ResponseCookie accessCookie = cookieUtil.deleteToken(Constants.ACCESS_TOKEN);
        ResponseCookie refreshCookie = cookieUtil.deleteToken(Constants.REFRESH_TOKEN);

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

}
