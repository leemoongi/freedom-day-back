package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.domain.loan.dto.LoanSimpleDto;
import com.side.freedomdaybackend.domain.loan.dto.MyLoanInfoDto;
import com.side.freedomdaybackend.domain.loan.dto.StatisticsDto;
import com.side.freedomdaybackend.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;
    private final LoanMapstruct loanMapstruct;

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
        List<StatisticsDto.RepaymentHistoryMonthTmp> repaymentHistoryMonthTmpList = loanRepository.repaymentHistoryMonthTmp(memberId);

        // 데이터 가공중
        LocalDate now = LocalDate.now();
        for (StatisticsDto.LoanSimpleTmp loanSimpleTmp : loanTmpList) {
            int paymentDate = loanSimpleTmp.getPaymentDate();


            int d_day = repaymentCountdown(paymentDate, now);
            loanSimpleTmp.setPaymentDDay(d_day);


        }



        return statisticsDto;


    }


     // 상환일로 D-day 계산
    public int repaymentCountdown(int dateNumber, LocalDate now) {
        LocalDate paymentDate = LocalDate.of(now.getYear(), now.getMonth(), dateNumber);
        int dayOfMonth = now.getDayOfMonth();

        if (dayOfMonth > dateNumber) {
            paymentDate = paymentDate.plusMonths(1);
        }

        return (int) ChronoUnit.DAYS.between(now, paymentDate);
    }




}
