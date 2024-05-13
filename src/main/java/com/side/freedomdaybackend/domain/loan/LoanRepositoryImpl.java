package com.side.freedomdaybackend.domain.loan;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.side.freedomdaybackend.domain.loan.dto.LoanSimpleDto;
import com.side.freedomdaybackend.domain.loan.dto.LoanStatisticsDto;
import com.side.freedomdaybackend.domain.loan.dto.QLoanSimpleDto;
import com.side.freedomdaybackend.domain.member.Member;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.side.freedomdaybackend.domain.loan.QLoan.loan;
import static com.side.freedomdaybackend.domain.loan.loanRepaymentMonthHistory.QLoanRepaymentMonthHistory.loanRepaymentMonthHistory;
import static com.side.freedomdaybackend.domain.member.QMember.member;

@RequiredArgsConstructor
public class LoanRepositoryImpl implements LoanRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Member queryDslTest() {
        return null;
    }

    @Override
    public List<LoanSimpleDto> findByLoanList(Long memberId) {

        return queryFactory
                .select(
                        new QLoanSimpleDto(
                                loan.id
                                , loan.name
                                , loan.purpose
                                , loan.bankCode
                                , loan.totalPrincipal
                                , loan.repaymentAmount
                                , loan.expirationDate
                                , loan.paymentDate
                        )
                )
                .from(loan)
                .where(loan.member.id.eq(memberId),
                        loan.status.eq('0'))
                .fetch();
    }

    @Override
    public Optional<Long> findByPreviousMonthPayment(Long memberId) {
        LocalDate previousMonth = LocalDate.now().minusMonths(1);
        LocalDate start = LocalDate.of(previousMonth.getYear(), previousMonth.getMonth(), 1);
        LocalDate end = LocalDate.of(previousMonth.getYear(), previousMonth.getMonth(), 1)
                .plusMonths(1).withDayOfMonth(1).minusDays(1);

        return Optional.ofNullable(queryFactory
                .select(
                        loanRepaymentMonthHistory.repaymentAmount1.sum()
                                .add(loanRepaymentMonthHistory.repaymentAmount2.sum())
                                .add(loanRepaymentMonthHistory.repaymentAmount3.sum())
                )
                .from(member)
                .leftJoin(loan)
                .on(member.eq(loan.member))
                .leftJoin(loanRepaymentMonthHistory)
                .on(loan.eq(loanRepaymentMonthHistory.loan))
                .where(
                        loan.member.id.eq(memberId)
                        , loanRepaymentMonthHistory.historyDate.between(start, end)
                        , loan.status.eq('0'))
                .groupBy(member.id)
                .fetchOne());
    }

    @Override
    public LoanStatisticsDto statistics(Long memberId) {
        return queryFactory
                .select(
                        Projections.fields(LoanStatisticsDto.class
                                , loan.totalPrincipal.sum().as("totalPrincipal")
                                , loan.repaymentAmount.sum().as("totalPrincipalRepayment")
                        )
                )
                .from(loan)
                .where(
                        loan.member.id.eq(memberId)
                        , loan.status.eq('0'))
                .fetchOne();
    }

    @Override
    public List<LoanStatisticsDto.LoanSimpleTmp> loanSimple(Long memberId) {
        // 상환 예정
        return queryFactory
                .select(
                        Projections.fields(LoanStatisticsDto.LoanSimpleTmp.class
                                , loan.name
                                , loan.purpose
                                , loan.paymentDate
                        )
                )
                .from(loan)
                    .leftJoin(loanRepaymentMonthHistory)
                    .on(loan.id.eq(loanRepaymentMonthHistory.loan.id))
                .where(
                        loan.member.id.eq(memberId)
                        , loanRepaymentMonthHistory.id.isNull()
                        , loan.status.eq('0'))
                .groupBy(loan.id)
                .fetch();

    }


    @Override
    public List<LoanStatisticsDto.RepaidLoan> repaidLoan(Long memberId) {
        LocalDate previousMonth = LocalDate.now().minusMonths(1);
        LocalDate start = LocalDate.of(previousMonth.getYear(), previousMonth.getMonth(), 1);
        LocalDate end = LocalDate.of(previousMonth.getYear(), previousMonth.getMonth(), 1)
                .plusMonths(1).withDayOfMonth(1).minusDays(1);

        // 이번달 상환 내역
        return queryFactory
                .select(
                        Projections.fields(LoanStatisticsDto.RepaidLoan.class
                                , loan.name
                                , loan.purpose
                                , loanRepaymentMonthHistory.repaymentAmount1.sum()
                                                .add(loanRepaymentMonthHistory.repaymentAmount2.sum())
                                                .add(loanRepaymentMonthHistory.repaymentAmount3.sum()).as("repaymentAmount"))
                )
                .from(loan)
                .leftJoin(loanRepaymentMonthHistory)
                .on(loan.id.eq(loanRepaymentMonthHistory.loan.id))
                .where(
                        loan.member.id.eq(memberId)
                        , loanRepaymentMonthHistory.historyDate.between(start, end)
                )
                .groupBy(loan.id)
                .fetch();

    }

    @Override
    public List<LoanStatisticsDto.RemainingPrincipal> remainingPrincipal(Long memberId) {
        return queryFactory
                .select(
                        Projections.fields(LoanStatisticsDto.RemainingPrincipal.class
                                , loan.purpose
                                , loan.totalPrincipal.sum().subtract(loan.repaymentAmount.sum()).as("remainingPrincipal"))
                )
                .from(loan)
                .where(loan.member.id.eq(memberId)
                        , loan.status.eq('0'))
                .groupBy(loan.purpose)
                .fetch();

    }

    @Override
    public List<LoanStatisticsDto.RepaymentHistoryMonth> repaymentHistoryList(long memberId) {
        return queryFactory
                .select(
                        Projections.fields(LoanStatisticsDto.RepaymentHistoryMonth.class
                                , loanRepaymentMonthHistory.historyDate
                                , loanRepaymentMonthHistory.repaymentAmount1
                                , loanRepaymentMonthHistory.repaymentAmount2
                                , loanRepaymentMonthHistory.repaymentAmount3)

                )
                .from(loan)
                .leftJoin(loanRepaymentMonthHistory)
                .on(loan.id.eq(loanRepaymentMonthHistory.loan.id))
                .where(loan.member.id.eq(memberId))
                .groupBy(loanRepaymentMonthHistory.historyDate)
                .fetch();
    }
}
