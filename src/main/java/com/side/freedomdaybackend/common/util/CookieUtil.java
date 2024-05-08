package com.side.freedomdaybackend.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CookieUtil {

    public ResponseCookie createToken(String cookieName, String token) {
        return ResponseCookie
                .from(cookieName, token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None") //개발 완료후 제거
                .maxAge(-1)
                .build();
    }

    public ResponseCookie deleteToken(String cookieName) {
        return ResponseCookie
                .from(cookieName)
                .path("/")
                .maxAge(0)
                .build();
    }

    public static String getCookieValue(HttpServletRequest request, String cookieNm) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return "";

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieNm))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");
    }

}
