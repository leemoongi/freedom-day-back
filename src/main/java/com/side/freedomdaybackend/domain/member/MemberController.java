package com.side.freedomdaybackend.domain.member;

import com.side.freedomdaybackend.common.constants.Constants;
import com.side.freedomdaybackend.common.response.ApiResponse;
import com.side.freedomdaybackend.common.util.CookieUtil;
import com.side.freedomdaybackend.common.util.JwtUtil;
import com.side.freedomdaybackend.common.util.RedisUtil;
import com.side.freedomdaybackend.domain.member.dto.SignInRequestDto;
import com.side.freedomdaybackend.domain.member.dto.SignInResponseDto;
import com.side.freedomdaybackend.domain.member.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
    private final RedisUtil redisUtil;

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse> signIn(@RequestBody SignInRequestDto signInRequestDto) throws NoSuchAlgorithmException {

        Member member = memberService.signIn(signInRequestDto);

        // 3. 토큰 발급
        String uuid = UUID.randomUUID().toString();
        String accessToken = jwtUtil.createAccessToken(String.valueOf(member.getId()));
        String refreshToken = jwtUtil.createRefreshToken(String.valueOf(member.getId()), uuid);

        // 4. 리프레쉬 토큰 저장
        redisUtil.set(uuid, refreshToken);

        // 5. 헤더에 추가
        ResponseCookie accessCookie = cookieUtil.createToken(Constants.ACCESS_TOKEN, accessToken);
        ResponseCookie refreshCookie = cookieUtil.createToken(Constants.REFRESH_TOKEN, refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        SignInResponseDto signInResponseDto = new SignInResponseDto(member.getNickName());
        ApiResponse apiResponse = new ApiResponse(signInResponseDto);

        return new ResponseEntity<>(apiResponse, headers, HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ApiResponse<String> signUp(@RequestBody SignUpRequestDto signUpRequestDto) throws NoSuchAlgorithmException {
        memberService.signUp(signUpRequestDto);
        return new ApiResponse<>();
    }

    @GetMapping("/email-authentication")
    public ApiResponse<String> emailAuthentication() {
        // TODO) 작업중
        return new ApiResponse<>("test");
    }



}
