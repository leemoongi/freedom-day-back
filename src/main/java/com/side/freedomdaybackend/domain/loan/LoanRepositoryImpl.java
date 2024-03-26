package com.side.freedomdaybackend.domain.loan;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.side.freedomdaybackend.domain.member.Member;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.side.freedomdaybackend.domain.loan.QLoan.loan;
import static com.side.freedomdaybackend.domain.loan.loanRepaymentMonthHistory.QLoanRepaymentMonthHistory.loanRepaymentMonthHistory;

@RequiredArgsConstructor
public class LoanRepositoryImpl implements LoanRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Member queryDslTest() {
        return null;
    }

    @Override
    public List<Loan> findByLoanList(Long memberId) {
        List<Loan> fetch = queryFactory
                .select(loan)
                .from(loan)
                    .leftJoin(loanRepaymentMonthHistory)
                    .on(loan.id.eq(loanRepaymentMonthHistory.loan.id))
                .where(loan.id.eq(1L))
                .fetch();

        return fetch;
    }
}
