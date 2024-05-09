package com.side.freedomdaybackend.domain.loan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanCreateDto {

    private String name; // 대출 이름
    private String purpose; // 대출 목적
    private String bankCode; // 은행
    private long totalPrincipal; // 총 원금
    private long repaymentAmount; // 상환 완료 금액
    private double interestRate; // 연 이자율
//    @JsonProperty("isVariableRate")
    private boolean variableRate; // 변동금리여부
    private int loanPeriod; // 대출 기간
    private LocalDate originationDate; // 시작 일시
    private LocalDate expirationDate; // 상환 일시
    private int paymentDate; // 납부일   매월 15일에 납부 -> 15
    private char periodUnit; // 대출 기간 단위   월:M 일:D

}
