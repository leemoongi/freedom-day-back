package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.domain.loan.dto.LoanSimpleDto;
import com.side.freedomdaybackend.domain.loan.dto.MyLoanInfoDto;
import com.side.freedomdaybackend.domain.loan.dto.StatisticsDto;
import com.side.freedomdaybackend.mapper.LoanMapper;
import com.side.freedomdaybackend.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;
    private final LoanMapstruct loanMapstruct;
    private final LoanMapper loanMapper;

    public MyLoanInfoDto myLoanList(long memberId) {

        // 내 대출 리스트 정보
        List<LoanSimpleDto> loanSimpleDtoList = loanRepository.findByLoanList(memberId);

        int repaymentRate = 0; // 상환률 = 상환금액 / 대출원금 * 100
        double totalPayment = 0; // 대출원금
        double repaymentAmount = 0; // 상환금액
        double loanAmount = 0; // 남은 원금
        int loanCount = 0; // 개수
        Long previousMonthPayment = loanRepository.findByPreviousMonthPayment(memberId);; // 지난달 총 납부액

        LocalDate now = LocalDate.now();
        for (LoanSimpleDto loanSimpleDto : loanSimpleDtoList) {
            // 남은 납부일 계산 D-day
            int dayOfMonth = now.getDayOfMonth();
            LocalDate paymentDate = LocalDate.of(now.getYear(), now.getMonth(), loanSimpleDto.getPaymentDate());
            if (dayOfMonth > loanSimpleDto.getPaymentDate()) {
                paymentDate = paymentDate.plusMonths(1);
            }
            long d_day = ChronoUnit.DAYS.between(now, paymentDate);
            loanSimpleDto.setPaymentDDay((int) d_day);

            // 남은 원금 계산
            long principal = loanSimpleDto.getTotalPrincipal() - loanSimpleDto.getRepaymentAmount();
            loanSimpleDto.setOutstandingPrincipal(principal);

            totalPayment = totalPayment + loanSimpleDto.getTotalPrincipal();
            repaymentAmount = repaymentAmount + loanSimpleDto.getRepaymentAmount();
            loanSimpleDto.setPaymentPercentage((int) ((double) loanSimpleDto.getRepaymentAmount() / (double) loanSimpleDto.getTotalPrincipal() * 100)); //
        }

        repaymentRate = (int) (repaymentAmount / totalPayment * 100);
        loanCount = loanSimpleDtoList.size();

        // api 객체로 변경
        List<MyLoanInfoDto.LoanSimpleDto> loanSimpleDto = loanMapstruct.toLoanSimpleDto(loanSimpleDtoList);
        MyLoanInfoDto myLoanInfoDto = new MyLoanInfoDto(previousMonthPayment, repaymentRate, loanCount, loanSimpleDto);

        return myLoanInfoDto;
    }

    public StatisticsDto statistics(long memberId) {
        StatisticsDto statisticsDto = loanRepository.statistics(memberId);
        List<StatisticsDto.LoanSimpleTmp> loanTmpList = loanRepository.loanSimple(memberId);
        List<StatisticsDto.RepaidLoan> repaidLoan = loanRepository.repaidLoan(memberId);

        // 총 남은 원금 = 대출 금액 - 대출 상환 금액
        statisticsDto.setTotalRemainingPrincipal(statisticsDto.getTotalPrincipal() - statisticsDto.getTotalPrincipalRepayment());

        /* LoanList 상환 예정 */
        LocalDate now = LocalDate.now();
        List<StatisticsDto.LoanSimple> loanSimpleList = new ArrayList<StatisticsDto.LoanSimple>();
        for (StatisticsDto.LoanSimpleTmp tmp : loanTmpList) {
            int paymentNumber = tmp.getPaymentDate(); // ex) 16
            int d_day = repaymentCountdown(paymentNumber, now);
            LocalDate paymentDate = getRepaymentDate(paymentNumber, now);

            StatisticsDto.LoanSimple loanSimple = new StatisticsDto.LoanSimple(
                    tmp.getName()
                    , tmp.getPurpose()
                    , d_day
                    , paymentDate
            );

            loanSimpleList.add(loanSimple);
        }

        /* RepaymentHistoryMonthList 월별 상환 기록 */
        List<StatisticsDto.RepaymentHistoryMonthTmp> rhmTmpList = loanMapper.selectRepaymentHistoryList(memberId); // 임시
        List<StatisticsDto.RepaymentHistoryMonth> rhmList = new ArrayList<StatisticsDto.RepaymentHistoryMonth>(); // response 객체
        StatisticsDto.RepaymentHistoryMonth rhm = new StatisticsDto.RepaymentHistoryMonth();
        // 첫번째 먼저 처리
        if (rhmTmpList.size() > 0) {
            StatisticsDto.RepaymentHistoryMonthTmp firstTmp = rhmTmpList.get(0);
            String historyDate = firstTmp.getHistoryDate();
            int type = firstTmp.getType();

            rhm.setHistoryDate(historyDate);
            switch (type) {
                case 1 -> rhm.setRepaymentAmount1(firstTmp.getRepaymentAmount());
                case 2 -> rhm.setRepaymentAmount2(firstTmp.getRepaymentAmount());
                case 3 -> rhm.setRepaymentAmount3(firstTmp.getRepaymentAmount());
            }
        }

        for (int i = 1; i < rhmTmpList.size(); i++) {
            StatisticsDto.RepaymentHistoryMonthTmp tmp = rhmTmpList.get(i);
            int type = tmp.getType();
            String historyDate = tmp.getHistoryDate();

            // 새로운 날짜면 객체 새로 생성
            if (!historyDate.equals(rhm.getHistoryDate())) {
                rhmList.add(rhm);
                rhm = new StatisticsDto.RepaymentHistoryMonth();
                rhm.setHistoryDate(historyDate);
            }

            switch (type) {
                case 1 -> rhm.setRepaymentAmount1(tmp.getRepaymentAmount());
                case 2 -> rhm.setRepaymentAmount2(tmp.getRepaymentAmount());
                case 3 -> rhm.setRepaymentAmount3(tmp.getRepaymentAmount());
            }
        }

        /* RemainingPrincipal 대출 원금 비중 */
        List<StatisticsDto.RemainingPrincipal> rpList = loanRepository.remainingPrincipal(memberId);
        long total = statisticsDto.getTotalRemainingPrincipal();

        // 퍼센테이지 구하기
        for (StatisticsDto.RemainingPrincipal rp : rpList) {
            long remainingPrincipal = rp.getRemainingPrincipal();
            double percentage = ((double) remainingPrincipal / total) * 100;
            rp.setPercentage((int) percentage);
        }

        statisticsDto.setLoanList(loanSimpleList);
        statisticsDto.setRepaidLoanList(repaidLoan);
        statisticsDto.setRepaymentHistoryMonthList(rhmList);
        statisticsDto.setRemainingPrincipalList(rpList);

        return statisticsDto;
    }


    // 상환일로 D-day 계산
    public int repaymentCountdown(int paymentNumber, LocalDate now) {
        // 상환 날짜
        LocalDate date = LocalDate.of(now.getYear(), now.getMonth(), paymentNumber);

        // 상환날자가 지났을 경우, 한달 더함
        if (isPaidThisMonth(paymentNumber, now)) {
            date = date.plusMonths(1);
        }

        return (int) ChronoUnit.DAYS.between(now, date);
    }


     // 상환일로 D-day 계산
    public LocalDate getRepaymentDate(int paymentNumber, LocalDate now) {
        // 상환 날짜
        LocalDate date = LocalDate.of(now.getYear(), now.getMonth(), paymentNumber);

        // 상환날자가 지났을 경우, 한달 더함
        if (isPaidThisMonth(paymentNumber, now)) {
            date = date.plusMonths(1);
        }
        return date;
    }


    public boolean isPaidThisMonth(int paymentNumber, LocalDate now) {
        int currentDay = now.getDayOfMonth();

        // 현재 날짜가 상환날짜보다 크다 => 이미 지급했음
        if (currentDay > paymentNumber) return true;
        return false;
    }



}
