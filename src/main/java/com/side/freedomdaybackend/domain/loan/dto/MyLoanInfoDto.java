package com.side.freedomdaybackend.domain.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyLoanInfoDto {
    private long previousMonthPayment; // 지난달 총 납부금액
    private int repaymentRate; // 상활률
    private int loanCount; // 대출 개수
    private List<LoanSimpleDto> loanSimpleDtoList;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoanSimpleDto {
        String name; // 대출이름
        String purpose; // 용도
        int paymentDDay; // 남은 납부일
        int outstandingPrincipal; // 남은 원금
        LocalDateTime expirationDate; // 만기일
        int paymentDate; // 납부일
    }

}
