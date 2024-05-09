package com.side.freedomdaybackend.domain.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyLoanInfoDto {
    private Long previousMonthPayment; // 지난달 총 납부금액
    private int repaymentRate; // 상활률
    private int loanCount; // 대출 개수
    private List<LoanSimpleDto> loanSimpleDtoList;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoanSimpleDto {
        private long id;
        private String name; // 대출이름
        private String purpose; // 용도
        private int paymentDDay; // 남은 납부일
        private int outstandingPrincipal; // 남은 원금
        private LocalDate expirationDate; // 만기일
        private int paymentDate; // 납부일
        private int paymentPercentage; // 납부 진행률
    }

}
