package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.domain.loan.loanRepaymentMonthHistory.LoanRepaymentMonthHistory;
import com.side.freedomdaybackend.domain.loan.loanRepaymentMonthHistory.LoanRepaymentMonthHistoryRepository;
import com.side.freedomdaybackend.domain.member.Member;
import com.side.freedomdaybackend.domain.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class LoanServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    LoanRepaymentMonthHistoryRepository loanRepaymentMonthHistoryRepository;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .name("이문기")
                .phoneNumber("01012341234")
                .nickName("LeeMoonGi")
                .sex(true)
                .build();

        Loan loan = Loan.builder()
                .member(member)
                .name("카카오대출")
                .purpose("생활비")
                .totalPrincipal(19886317L)
                .paymentDate(16)
                .repaymentDate(LocalDateTime.now().plusYears(1))
                .build();

        LoanRepaymentMonthHistory loanRepaymentMonthHistory = LoanRepaymentMonthHistory.builder()
                .loan(loan)
                .type(1)
                .repaymentAmount(20000L)
                .build();

        memberRepository.save(member);
        loanRepository.save(loan);
        loanRepaymentMonthHistoryRepository.save(loanRepaymentMonthHistory);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void myLoanList() {
        // 유저 저회
        Member member = memberRepository.findById(1L).get();

        // 대출 정보
        List<Loan> byLoanList = loanRepository.findByLoanList(member.getId());
        System.out.println("byLoanList = " + byLoanList);
    }
}