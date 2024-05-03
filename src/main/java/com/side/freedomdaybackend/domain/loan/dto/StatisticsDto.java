package com.side.freedomdaybackend.domain.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDto {
    long totalBalance; // 총 대출 잔액
    long totalPrincipalRepayment; // 총 상환 원금
    List<LoanSimple> loanList; // 상환 예정
    List<RepaidLoan> repaidLoanList; // 상환 완료
    List<RepaymentHistoryMonth> repaymentHistoryMonthList; // 월별 상환 기록
    List<RemainingPrincipal> remainingPrincipalList; // 대출 원금 비중


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoanSimple {
        String name; // 대출 이름
        String purpose; // 용도
        int paymentDDay; // 남은 납부일
        int paymentDate; // 납부일자
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RepaidLoan {
        String name; // 대출 이름
        String purpose; // 용도
        long repaymentAmount; // 납부한 금액
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RepaymentHistoryMonth {
        LocalDateTime dateTime;
        int repaymentAmount1; // 상환 방식 -> 1:원금 2:이자 3:중도상환
        int repaymentAmount2;
        int repaymentAmount3;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RemainingPrincipal {
        String name; // 이름
        long amount; // 남은 금액
        int percentage; // 백분율
    }


    /**
     *  비지니스 로진에서 사용해야하는 dto
     */


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoanSimpleTmp {
        String name; // 대출 이름
        String purpose; // 용도
        int paymentDate; // 납부일자
        int paymentDDay; // 남은 납부일
        LocalDate repaymentDate; // 상환 예정일
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RepaidLoanTmp {
        String name; // 대출 이름
        String purpose; // 용도
        long amount; // 납부한 금액
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RepaymentHistoryMonthTmp {
        LocalDateTime dateTime;
        int repaymentAmount1; // 상환 방식 -> 1:원금 2:이자 3:중도상환
        int repaymentAmount2;
        int repaymentAmount3;
    }
}
