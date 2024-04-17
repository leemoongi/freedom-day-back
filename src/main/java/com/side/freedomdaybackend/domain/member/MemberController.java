package com.side.freedomdaybackend.domain.member;

import com.side.freedomdaybackend.common.response.ApiResponse;
import com.side.freedomdaybackend.domain.member.dto.MemberDto;
import com.side.freedomdaybackend.domain.member.dto.SignInRequestDto;
import com.side.freedomdaybackend.domain.member.dto.SignInResponseDto;
import com.side.freedomdaybackend.domain.member.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final MemberMapstruct memberMapstruct;

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody SignInRequestDto signInRequestDto) throws NoSuchAlgorithmException {
        // TODO) 작업중
        return memberService.signIn(signInRequestDto);
    }

    @PostMapping("/sign-up")
    public ApiResponse<String> signUp(@RequestBody SignUpRequestDto signUpRequestDto) throws NoSuchAlgorithmException {
        memberService.signUp(signUpRequestDto);
        return new ApiResponse<>("test");
    }

    @GetMapping("/email-authentication")
    public ApiResponse<String> emailAuthentication(SignUpRequestDto signUpRequestDto) {
        // TODO) 작업중
        return new ApiResponse<>("test");
    }

    @GetMapping("/list")
    public ApiResponse<List<MemberDto>> memberList() {
        List<Member> memberList = memberRepository.findAll();
        List<MemberDto> memberDtoList = memberMapstruct.toMemberDtoList(memberList);
        return new ApiResponse<>(memberDtoList);
    }

    @GetMapping("/querydsl-test")
    public ApiResponse<MemberDto> queryDslTest() {
        Member member = memberRepository.queryDslTest();
        MemberDto memberDto = memberMapstruct.toMemberDto(member);
        return new ApiResponse<>(memberDto);
    }
}
