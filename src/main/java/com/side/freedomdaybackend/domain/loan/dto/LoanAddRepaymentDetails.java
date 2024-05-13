package com.side.freedomdaybackend.domain.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanAddRepaymentDetails {

    private long loanId;
    private int interestRates; // 금리
    private long interestPayment; // 납입이자
    private long principalPayment; // 납입원금
    private long earlyRepayment; // 중도상환

}
