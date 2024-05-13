package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.domain.loan.dto.LoanSimpleDto;
import com.side.freedomdaybackend.domain.loan.dto.LoanStatisticsDto;
import com.side.freedomdaybackend.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface LoanRepositoryCustom {
    public Member queryDslTest();

    List<LoanSimpleDto> findByLoanList(Long memberId);
    Optional<Long> findByPreviousMonthPayment(Long memberId);
    LoanStatisticsDto statistics(Long memberId);
    List<LoanStatisticsDto.LoanSimpleTmp> loanSimple(Long memberId);
    List<LoanStatisticsDto.RepaidLoan> repaidLoan(Long memberId);
    List<LoanStatisticsDto.RemainingPrincipal> remainingPrincipal(Long memberId);
    List<LoanStatisticsDto.RepaymentHistoryMonth> repaymentHistoryList(long memberId);
}
