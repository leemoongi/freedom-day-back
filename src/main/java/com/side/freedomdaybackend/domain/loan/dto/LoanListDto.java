package com.side.freedomdaybackend.domain.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoanListDto {
    private Long id;
    private String name; // 대출 이름
    private String purpose; // 대출 목적
    private String bankCode; // 은행
    private Long totalPrincipal; // 총 원금
    private Long repaymentAmount; // 상환 완료 금액
    private int interestRate; // 연 이자율
    private int loanPeriod; // 연 이자율
    private Boolean variableRate; // 변동금리 여부
    private LocalDateTime originationDate; // 대출 기간  TODO) 기간에 대한 정보를 어떻게 저장할지 결졍해야함
    private LocalDateTime repaymentDate; // 시작 일시
    private int paymentDate; // 납부일  매월 15일에 납부 -> 15
}
