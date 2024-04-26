package com.side.freedomdaybackend.common.util;

import com.side.freedomdaybackend.common.constants.Constants;
import com.side.freedomdaybackend.common.exception.CustomException;
import com.side.freedomdaybackend.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final JwtUtil jwtUtil;

    public long checkAuth(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String accessToken = null;

        if (cookies.length == 0)
            throw new CustomException(ErrorCode.JWT_ERROR);

        for (Cookie cookie : cookies) {
            if (Constants.ACCESS_TOKEN.equals(cookie.getName())) accessToken = cookie.getValue();
        }

        if (accessToken == null)
            throw new CustomException(ErrorCode.JWT_ERROR);

        Claims claims = jwtUtil.isValidToken(accessToken);
        String memberId = (String) claims.get(Constants.MEMBER_ID);
        return Long.valueOf(memberId);
    }
}
