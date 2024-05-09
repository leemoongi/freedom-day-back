package com.side.freedomdaybackend.domain.loan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class LoanSimpleDto {
    private Long id;
    private String name; // 대출 이름
    private String purpose; // 대출 목적
    private String bankCode; // 은행
    private Long totalPrincipal; // 총 원금
    private Long repaymentAmount; // 상환 완료 금액
    private LocalDate expirationDate; // 시작 일시
    private int paymentDate; // 납부일  매월 15일에 납부 -> 15
    private int paymentDDay; // 남은 납부일 D-day
    private Long outstandingPrincipal; // 남은 원금
    private int paymentPercentage; // 납부 진행률

    @QueryProjection
    public LoanSimpleDto(Long id, String name, String purpose, String bankCode, Long totalPrincipal, Long repaymentAmount, LocalDate expirationDate, int paymentDate) {
        this.id = id;
        this.name = name;
        this.purpose = purpose;
        this.bankCode = bankCode;
        this.totalPrincipal = totalPrincipal;
        this.repaymentAmount = repaymentAmount;
        this.expirationDate = expirationDate;
        this.paymentDate = paymentDate;
    }
}

