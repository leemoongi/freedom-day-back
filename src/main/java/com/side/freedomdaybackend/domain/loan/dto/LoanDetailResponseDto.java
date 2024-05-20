package com.side.freedomdaybackend.domain.loan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.side.freedomdaybackend.domain.loan.RepaymentMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDetailResponseDto {
    private String name; // 이름
    private String purpose; // 목적
    private String bankCode; // 은행 코드
    private long outstandingPrincipal; // 남은 원금
    private long repaymentAmount; // 상환 완료 금액
    private List<RepaymentHistoryMonth> repaymentHistoryMonthList;
    private long totalPrincipal; // 대출 금액
    private LocalDate originationDate; // 대출 실행일
    private long loanPeriod; // 대출 기간
    private double interestRate; // 이자율
    private String repaymentMethod; // 상환 방식

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RepaymentHistoryMonth {
        @JsonFormat(pattern = "yyyy-MM")
        private LocalDate historyDate; // 등록 월 ex) yyyy-mm
        private double interestRate; // 이자율
        private long repaymentAmount1; // 상환 방식 -> 1:원금 2:이자 3:중도상환
        private long repaymentAmount2;
        private long repaymentAmount3;
        private boolean delayed; // 연체 여부 true: 연체됨
    }


    /******************************************
     *****  비지니스 로직에서 값 수정 용도 dto  *****
     ******************************************/
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class Tmp {
//        private long name; // 이름
//        private long purpose; // 목적
//        private long bankCode; // 은행 코드
//        private long totalPrincipal; // 대출 금액
//        private long repaymentAmount; // 상환 완료 금액
//        private long percentage; // 퍼센트
//
//        private LocalDate originationDate; // 시작 날짜
//        private LocalDate expirationDate; // 종료 날짜
//        private long loanPeriod; // 대출 기간
//        private long interestRate; // 이자율
//    }


}
