package com.side.freedomdaybackend.domain.loan;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.side.freedomdaybackend.domain.loan.dto.LoanSimpleDto;
import com.side.freedomdaybackend.domain.loan.dto.LoanStatisticsDto;
import com.side.freedomdaybackend.domain.loan.dto.QLoanSimpleDto;
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
    public Long findByPreviousMonthPayment(Long memberId) {
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
                .where(
                        loan.member.id.eq(memberId)
                        , loanRepaymentMonthHistory.historyDate.between(start, end)
                        , loan.status.eq('0'))
                .groupBy(loan.id)
                .fetchOne();
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
                .where(
                        loan.member.id.eq(memberId)
                        , loan.status.eq('0'))
                .fetch();

    }


    @Override
    public List<LoanStatisticsDto.RepaidLoan> repaidLoan(Long memberId) {
        // 상환 완료
        return queryFactory
                .select(
                        Projections.fields(LoanStatisticsDto.RepaidLoan.class
                                , loan.name
                                , loan.purpose
                                , loan.repaymentAmount)
                )
                .from(loan)
                .where(
                        loan.member.id.eq(memberId)
                        , loan.status.eq('1'))
                .fetch();

    }
//
//    @Override
//    public List<LoanStatisticsDto.RepaymentHistoryMonthTmp> repaymentHistoryMonthTmp(Long memberId) {
//        // 월별 상환 기록
//        return queryFactory
//                .select(
//                        Projections.fields(LoanStatisticsDto.RepaymentHistoryMonthTmp.class
//                                , loanRepaymentMonthHistory.historyDate
//                                , loanRepaymentMonthHistory.repaymentAmount.sum()
//                                , loanRepaymentMonthHistory.type
//                        )
//                )
//                .from(loan)
//                .leftJoin(loanRepaymentMonthHistory)
//                .on(loan.id.eq(loanRepaymentMonthHistory.loan.id))
//                .where(loan.member.id.eq(memberId))
//                .groupBy(loanRepaymentMonthHistory.historyDate, loanRepaymentMonthHistory.type)
//                .fetch();
//
//    }

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
}
