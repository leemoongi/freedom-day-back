package com.side.freedomdaybackend.common.util;

import com.side.freedomdaybackend.common.constants.Constants;
import com.side.freedomdaybackend.common.exception.CustomException;
import com.side.freedomdaybackend.common.exception.ErrorCode;
import com.side.freedomdaybackend.domain.member.Member;
import com.side.freedomdaybackend.domain.member.MemberRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public Claims checkAuth(HttpServletRequest request) {
        Claims claims = getClaims(request);
        return claims;
    }

    public Long checkAuthReturnId(HttpServletRequest request) {
        Claims claims = getClaims(request);
        String memberIdStr = (String) claims.get(Constants.MEMBER_ID);
        Long memberId = Long.valueOf(memberIdStr);

        // 멤거가 존재하는지 체크
        if (!memberRepository.existsMemberId(memberId)) {
            throw new CustomException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        return memberId;
    }

    public Member checkAuthReturnMember(HttpServletRequest request) {
        Claims claims = getClaims(request);
        String memberIdStr = (String) claims.get(Constants.MEMBER_ID);
        Long memberId = Long.valueOf(memberIdStr);

        // 멤버 검색
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        return member;
    }


    private Claims getClaims(HttpServletRequest request) {
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
        return claims;
    }

}
