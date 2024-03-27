package com.side.freedomdaybackend.domain.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDto {
    private Long id;
    private String name; // 대출 이름
    private String purpose; // 대출 목적
    private String bankCode; // 은행
    private Long totalPrincipal; // 총 원금
    private Long repaymentAmount; // 상환 완료 금액
    private int interestRate; // 연 이자율
    private int loanPeriod; // 대출 기간
    private String variableRate; // 변동금리여부  0: false  1:true
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime originationDate; // 대출 기간  TODO) 기간에 대한 정보를 어떻게 저장할지 결졍해야함
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime expirationDate; // 시작 일시
    private int paymentDate; // 납부일  매월 15일에 납부 -> 15

    private int paymentDDay; // 남은 납부일 D-day
    private Long outstandingPrincipal; // 남은 원금

//    @QueryProjection
//    public LoanDto(Long id, String name, String purpose, String bankCode, Long totalPrincipal, Long repaymentAmount, int interestRate, int loanPeriod, String variableRate, LocalDateTime originationDate, LocalDateTime expirationDate, int paymentDate, int paymentDDay, Long principal) {
//        this.id = id;
//        this.name = name;
//        this.purpose = purpose;
//        this.bankCode = bankCode;
//        this.totalPrincipal = totalPrincipal;
//        this.repaymentAmount = repaymentAmount;
//        this.interestRate = interestRate;
//        this.loanPeriod = loanPeriod;
//        this.variableRate = variableRate;
//        this.originationDate = originationDate;
//        this.expirationDate = expirationDate;
//        this.paymentDate = paymentDate;
//        this.paymentDDay = paymentDDay;
//        this.principal = principal;
//    }
}

