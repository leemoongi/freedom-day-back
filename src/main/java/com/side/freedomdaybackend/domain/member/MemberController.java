package com.side.freedomdaybackend.domain.member;

import com.side.freedomdaybackend.common.response.ApiResponse;
import com.side.freedomdaybackend.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberMapstruct memberMapstruct;

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
