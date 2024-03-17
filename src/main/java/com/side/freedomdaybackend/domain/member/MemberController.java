package com.side.freedomdaybackend.domain.member;

import com.side.freedomdaybackend.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    public final MemberRepository memberRepository;

    @GetMapping("/list")
    public ApiResponse<List<Member>> memberList() {
        List<Member> memberList = memberRepository.findAll();
        return new ApiResponse<>(memberList);
    }

    @GetMapping("/querydsl-test")
    public ApiResponse<Member> queryDslTest() {
        Member member = memberRepository.queryDslTest();
        return new ApiResponse<>(member);
    }
}
