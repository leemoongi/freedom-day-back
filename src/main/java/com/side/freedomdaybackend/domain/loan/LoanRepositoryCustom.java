package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.domain.loan.dto.LoanSimpleDto;
import com.side.freedomdaybackend.domain.loan.dto.StatisticsDto;
import com.side.freedomdaybackend.domain.member.Member;

import java.util.List;

public interface LoanRepositoryCustom {
    public Member queryDslTest();

    List<LoanSimpleDto> findByLoanList(Long memberId);
    Long findByPreviousMonthPayment(Long memberId);
    StatisticsDto statistics(Long memberId);
    List<StatisticsDto.LoanSimpleTmp> loanSimple(Long memberId);
    List<StatisticsDto.RepaidLoan> repaidLoan(Long memberId);
    List<StatisticsDto.RemainingPrincipal> remainingPrincipal(Long memberId);
}
