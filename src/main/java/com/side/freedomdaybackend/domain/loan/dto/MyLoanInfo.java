package com.side.freedomdaybackend.domain.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyLoanInfo {
    private long previousMonthPayment; // 지난달 총 납부금액
    private int repaymentRate; // 상활률
    private int loanCount; // 대출 개수
    private List<LoanSimpleDto> loanSimpleDtoList;
}

class LoanSimpleDto {
    String loanName; // 대출이름
    String purpose; // 용도
    int paymentDDay; // 남은 납부일
    int loanAmount; // 남은 원금
    LocalDateTime loanExpirationDate; // 만기일
    int paymentDueDay; // 납부일
}