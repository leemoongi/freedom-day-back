package com.side.freedomdaybackend.domain.member;

import com.side.freedomdaybackend.common.constants.Constants;
import com.side.freedomdaybackend.common.response.ApiResponse;
import com.side.freedomdaybackend.common.util.CookieUtil;
import com.side.freedomdaybackend.common.util.JwtUtil;
import com.side.freedomdaybackend.domain.member.dto.MemberDto;
import com.side.freedomdaybackend.domain.member.dto.SignInRequestDto;
import com.side.freedomdaybackend.domain.member.dto.SignInResponseDto;
import com.side.freedomdaybackend.domain.member.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody SignInRequestDto signInRequestDto) throws NoSuchAlgorithmException {

        Member member = memberService.signIn(signInRequestDto);

        // 3. 토큰 발급
        String accessToken = jwtUtil.createAccessToken(member.getEmail());
        String refreshToken = jwtUtil.createRefreshToken(member.getEmail(), UUID.randomUUID().toString());

        // 4. TODO) 리프레쉬 토큰 Redis 저장

        // 5. 헤더에 추가
        ResponseCookie accessCookie = cookieUtil.createToken(Constants.ACCESS_TOKEN, accessToken);
        ResponseCookie refreshCookie = cookieUtil.createToken(Constants.REFRESH_TOKEN, refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        SignInResponseDto signInResponseDto = new SignInResponseDto(member.getId());

        return new ResponseEntity<>(signInResponseDto, headers, HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ApiResponse<String> signUp(@RequestBody SignUpRequestDto signUpRequestDto) throws NoSuchAlgorithmException {
        memberService.signUp(signUpRequestDto);
        return new ApiResponse<>("test");
    }

    @GetMapping("/email-authentication")
    public ApiResponse<String> emailAuthentication() {
        // TODO) 작업중
        return new ApiResponse<>("test");
    }



}
