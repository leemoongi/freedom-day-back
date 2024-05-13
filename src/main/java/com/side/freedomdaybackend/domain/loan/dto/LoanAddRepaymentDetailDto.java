package com.side.freedomdaybackend.domain.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanAddRepaymentDetailDto {

    private long loanId; // 대출 pk
    private int interestRates; // 금리
    private long repaymentAmount1; // 납입이자
    private long repaymentAmount2; // 납입원금
    private long repaymentAmount3; // 중도상환
    private LocalDate historyDate; // 상환일시

}
