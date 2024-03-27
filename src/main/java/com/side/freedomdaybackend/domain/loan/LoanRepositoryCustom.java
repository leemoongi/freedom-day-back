package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.domain.loan.loanRepaymentMonthHistory.LoanRepaymentMonthHistory;
import com.side.freedomdaybackend.domain.member.Member;

import java.util.List;

public interface LoanRepositoryCustom {
    public Member queryDslTest();

    List<Loan> findByLoanList(Long memberId);
    long findByPreviousMonthPayment(Long memberId);


}
