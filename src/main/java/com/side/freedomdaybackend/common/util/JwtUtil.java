package com.side.freedomdaybackend.common.util;

import com.side.freedomdaybackend.common.constants.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    @Value("${security.jwt-key}")
    private String key;

    public String createAccessToken(String memberId) {
        Date now = new Date();
//        Date expiration = new Date(now.getTime() + Duration.ofMinutes(10).toMillis()); // 10분
        Date expiration = new Date(now.getTime() + Duration.ofHours(3).toMillis()); // 3시간

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setExpiration(expiration) // 만료시간
                .claim(Constants.MEMBER_ID, memberId)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String createRefreshToken(String memberId, String uuid) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofHours(3).toMillis()); // 3시간

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setExpiration(expiration)
                .claim(Constants.MEMBER_ID, memberId)
                .claim(Constants.UUID, uuid)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public Claims isValidToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



}
