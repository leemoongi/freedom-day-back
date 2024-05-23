package com.side.freedomdaybackend.domain.member;

import com.side.freedomdaybackend.common.constants.Constants;
import com.side.freedomdaybackend.common.response.ApiResponse;
import com.side.freedomdaybackend.common.util.*;
import com.side.freedomdaybackend.domain.member.dto.EmailAuthenticationDto;
import com.side.freedomdaybackend.domain.member.dto.SignInRequestDto;
import com.side.freedomdaybackend.domain.member.dto.SignInResponseDto;
import com.side.freedomdaybackend.domain.member.dto.SignUpRequestDto;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final AuthUtil authUtil;
    private final EmailUtil emailutil;
    private final SpringTemplateEngine templateEngine;

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse> signIn(@RequestBody SignInRequestDto signInRequestDto) throws NoSuchAlgorithmException {

        Member member = memberService.signIn(signInRequestDto);

        // 3. 토큰 발급
        String uuid = UUID.randomUUID().toString();
        String accessToken = jwtUtil.createAccessToken(String.valueOf(member.getId()));
        String refreshToken = jwtUtil.createRefreshToken(String.valueOf(member.getId()), uuid);

        // 4. 리프레쉬 토큰 저장
        redisUtil.set(uuid, refreshToken, Duration.ofHours(3).toMillis());

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
    public ResponseEntity<ApiResponse> signUp(@RequestBody SignUpRequestDto signUpRequestDto) throws NoSuchAlgorithmException {
        /* 회원가입 */
        memberService.signUp(signUpRequestDto);

        /* 로그인 */
        SignInRequestDto signInRequestDto = new SignInRequestDto(
                signUpRequestDto.getEmail()
                , signUpRequestDto.getPassword()
        );
        ResponseEntity<ApiResponse> responseResponseEntity = this.signIn(signInRequestDto);

        return responseResponseEntity;
    }

    @PostMapping("/sign-out")
    public ApiResponse<String> signOut(HttpServletRequest request, HttpServletResponse response) {
        // UUID 추출
        String uuid = authUtil.getUUID(request);

        // 레디스에서 제거
        redisUtil.remove(uuid);

        // 쿠키제거
        ResponseCookie accessCookie = cookieUtil.deleteToken(Constants.ACCESS_TOKEN);
        ResponseCookie refreshCookie = cookieUtil.deleteToken(Constants.REFRESH_TOKEN);
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return new ApiResponse<>();
    }

    @PostMapping("/send-mail")
    public ApiResponse<String> emailAuthentication(@RequestBody EmailAuthenticationDto dto) throws MessagingException {
        emailutil.sendAuthUrlMail(dto);
        return new ApiResponse<>();
    }

    @GetMapping("/email-authentication")
    public ResponseEntity<String> emailAuthentication(@RequestParam("token") String token) {
        String htmlBody = emailutil.emailAuthentication(token);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8");

        return new ResponseEntity<>(htmlBody, headers, HttpStatus.OK);
    }




}
