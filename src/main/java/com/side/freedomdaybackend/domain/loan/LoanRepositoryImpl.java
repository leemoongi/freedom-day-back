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
                    .leftJoin(loanRepaymentMonthHistory)
                    .on(loan.id.eq(loanRepaymentMonthHistory.loan.id))
                .where(loan.id.eq(memberId))
                .fetch();

        return fetch;
    }

    @Override
    public List<LoanRepaymentMonthHistory> findByPreviousMonthPayment(Long memberId) {
        LocalDateTime previousMonth = LocalDateTime.now().minusMonths(1);
        LocalDateTime start = LocalDateTime.of(previousMonth.getYear(), previousMonth.getMonth(), 1, 0, 0);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);

        return queryFactory
                .select(loanRepaymentMonthHistory)
                .from(member)
                    .leftJoin(loan)
                         .on(member.eq(loan.member))
                    .leftJoin(loanRepaymentMonthHistory)
                        .on(loan.eq(loanRepaymentMonthHistory.loan))
                .where(loanRepaymentMonthHistory.historyDate.between(start, end),
                        loan.status.eq('0'))
                .fetch();
    }
}
