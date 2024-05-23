package com.side.freedomdaybackend.domain.loan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanStatisticsDto {
    private long totalPrincipal; // 총 대출 금액
    private long totalPrincipalRepayment; // 총 상환 원금
    private List<LoanSimple> loanList; // 상환 예정
    private List<RepaidLoan> repaidLoanList; // 상환 완료
    private List<RepaymentHistoryMonth> repaymentHistoryMonthList; // 월별 상환 기록
    private long totalRemainingPrincipal; // 남은 총 남은원금
    private List<RemainingPrincipal> remainingPrincipalList; // 대출 원금 비중


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoanSimple {
        private String name; // 대출 이름
        private String purpose; // 용도
        private int paymentDDay; // 남은 납부일 D-Day
        private LocalDate paymentDate; // 납부 예정일
        private boolean delayed; // 연체 확인
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RepaidLoan {
        private String name; // 대출 이름
        private String purpose; // 용도
        private long repaymentAmount; // 납부한 금액
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RepaymentHistoryMonth {
        // @Convert(converter = YearMonthConverter.class)
        @JsonFormat(pattern = "yyyy-MM")
        private LocalDate historyDate; // 등록 월 ex) yyyy-mm
        private long repaymentAmount1; // 상환 방식 -> 1:원금 2:이자 3:중도상환
        private long repaymentAmount2;
        private long repaymentAmount3;
//        private boolean delayed; // 연체 확인
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RemainingPrincipal {
        private String purpose; // 이름
        private long remainingPrincipal; // 남은 금액
        private int percentage; // 백분율
    }


    /******************************************
     *****  비지니스 로직에서 값 수정 용도 dto  *****
     ******************************************/
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tmp {
        private long totalPrincipal; // 총 대출 금액
        private long totalPrincipalRepayment; // 총 상환 원금
        private LocalDate originationDate; // 시작날짜
        private LocalDate expirationDate; // 종료날짜
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoanSimpleTmp {
        private String name; // 대출 이름
        private String purpose; // 용도
        private int paymentDDay; // 남은 납부일
        private int paymentDate; // 납부일자 ex) 16
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RepaymentHistoryMonthTmp {
        private LocalDate historyDate; // 등록 월 ex) yyyy-mm
        private long repaymentAmount1; // 상환 방식 -> 1:원금 2:이자 3:중도상환
        private long repaymentAmount2;
        private long repaymentAmount3;
    }

//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class RepaymentHistoryMonthTmp {
//        private LocalDate originationDate;
//        private LocalDate expirationDate;
//        private RepaymentHistoryMonth repaymentHistoryMonth;
//    }

}
