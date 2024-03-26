package com.side.freedomdaybackend.domain.loan.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class MyLoanListDto {
    String loanName; // 대출이름
    String purpose; // 용도
    int paymentDDay; // 남은 납부일
    int loanAmount; // 남은 원금
    LocalDateTime loanExpirationDate; // 만기일
    int paymentDueDay; // 납부일

    @QueryProjection
    public MyLoanListDto(String loanName, String purpose, int paymentDDay, int loanAmount, LocalDateTime loanExpirationDate, int paymentDueDay) {
        this.loanName = loanName;
        this.purpose = purpose;
        this.paymentDDay = paymentDDay;
        this.loanAmount = loanAmount;
        this.loanExpirationDate = loanExpirationDate;
        this.paymentDueDay = paymentDueDay;
    }
}
