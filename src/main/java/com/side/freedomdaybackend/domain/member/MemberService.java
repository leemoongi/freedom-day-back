package com.side.freedomdaybackend.domain.member;

import com.side.freedomdaybackend.common.constants.Constants;
import com.side.freedomdaybackend.common.exception.CustomException;
import com.side.freedomdaybackend.common.exception.ErrorCode;
import com.side.freedomdaybackend.common.util.CookieUtil;
import com.side.freedomdaybackend.common.util.EncryptUtil;
import com.side.freedomdaybackend.common.util.JwtUtil;
import com.side.freedomdaybackend.domain.member.dto.SignInRequestDto;
import com.side.freedomdaybackend.domain.member.dto.SignInResponseDto;
import com.side.freedomdaybackend.domain.member.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final EncryptUtil encryptUtil;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    public ResponseEntity<SignInResponseDto> signIn(SignInRequestDto signInRequestDto) throws NoSuchAlgorithmException {
        String email = signInRequestDto.getEmail();
        String password = signInRequestDto.getPassword();
        String encryptedPassword = encryptUtil.sha256(password); // 암호화

        Member member = memberRepository.findByEmailAndPassword(email, encryptedPassword)
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST)); // TODO) 에러코드 추가

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

        SignInResponseDto signInResponseDto = new SignInResponseDto();

        return new ResponseEntity<>(signInResponseDto, headers, HttpStatus.OK);
    }


    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) throws NoSuchAlgorithmException {
        // 1. 이메일 중복여부 검사
        String email = signUpRequestDto.getEmail();
        String password = signUpRequestDto.getPassword();

        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);  // TODO) 에러코드 추가
        };

        // 2. 비밀번호 안호화
        String encryptedPassword = encryptUtil.sha256(password);

        // 3. Member 테이블에 추가
        Member member = Member.builder()
                .email(email)
                .password(encryptedPassword)
                .nickName(signUpRequestDto.getNickName())
                .sex(signUpRequestDto.getSex())
                .birthDate(signUpRequestDto.getBirthDate())
                .status('0')
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now()).build();

        memberRepository.save(member);
    }
}
