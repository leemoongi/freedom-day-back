package com.side.freedomdaybackend.domain.loan;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.side.freedomdaybackend.domain.loan.loanRepaymentMonthHistory.LoanRepaymentMonthHistory;
import com.side.freedomdaybackend.domain.member.Member;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.side.freedomdaybackend.domain.loan.QLoan.loan;
import static com.side.freedomdaybackend.domain.loan.loanRepaymentMonthHistory.QLoanRepaymentMonthHistory.loanRepaymentMonthHistory;
import static com.side.freedomdaybackend.domain.member.QMember.*;

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
                .where(loan.member.id.eq(memberId),
                        loan.status.eq('0'))
                .fetch();

        return fetch;
    }

    @Override
    public long findByPreviousMonthPayment(Long memberId) {
        LocalDateTime previousMonth = LocalDateTime.now().minusMonths(1);
        LocalDateTime start = LocalDateTime.of(previousMonth.getYear(), previousMonth.getMonth(), 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(previousMonth.getYear(), previousMonth.getMonth(), 1, 0, 0)
                .plusMonths(1).withDayOfMonth(1).minusDays(1);

        return queryFactory
                .select(loanRepaymentMonthHistory.repaymentAmount.sum())
                .from(member)
                .leftJoin(loan)
                .on(member.eq(loan.member))
                .leftJoin(loanRepaymentMonthHistory)
                .on(loan.eq(loanRepaymentMonthHistory.loan))
                .where(loanRepaymentMonthHistory.historyDate.between(start, end),
                        loan.status.eq('0'))
                .groupBy(loan.id)
                .fetchOne();
    }
}
