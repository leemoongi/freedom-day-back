package com.side.freedomdaybackend.domain.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanSimpleDto {
    private Long id;
    private String name; // 대출 이름
    private String purpose; // 대출 목적
    private String bankCode; // 은행
    private Long totalPrincipal; // 총 원금
    private Long repaymentAmount; // 상환 완료 금액
    private int paymentDate; // 납부일  매월 15일에 납부 -> 15

    private int paymentDDay; // 남은 납부일 D-day
    private Long outstandingPrincipal; // 남은 원금
    private int paymentPercentage; // 납부 진행률

}

