package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.domain.member.Member;
import com.side.freedomdaybackend.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;

    public void myLoanList() {
        // 멤버의 id를 가져오나

        Member member = memberRepository.findById(1L).get();
        Long memberId = member.getId();

        // 그걸로 나의 대출리스트를 가져온다
        loanRepository.findByLoanList(memberId);


    }
}
