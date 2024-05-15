package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.common.converter.RepaymentMethodConverter;
import com.side.freedomdaybackend.domain.loan.loanRepaymentMonthHistory.LoanRepaymentMonthHistory;
import com.side.freedomdaybackend.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "loan")
    private List<LoanRepaymentMonthHistory> loanRepaymentMonthHistory;

    private String name; // 대출 이름
    private String purpose; // 대출 목적
    private String bankCode; // 은행
    private Long totalPrincipal; // 총 원금
    private Long repaymentAmount; // 상환 완료 금액
    private double interestRate; // 연 이자율
    private int loanPeriod; // 대출 기간  TODO) 기간에 대한 정보를 어떻게 저장할지 결졍해야함
    private boolean variableRate; // 변동금리여부  0: false  1:true
    private LocalDate originationDate; // 시작 일시
    private LocalDate expirationDate; // 상환 일시
    private int paymentDate; // 납부일  매월 15일에 납부 -> 15
    @Convert(converter = RepaymentMethodConverter.class)
    private RepaymentMethod repaymentMethod; // BR: 만기일시, EPI: 월리금균등, EP: 원금균등
    private Character status; // 0:진행중  1: 만료됨

    public void setCreateBefore(Member member) {
        this.member = member;
        this.status = '0';
    }

    public void addRepaymentAmount(double interestRate, long repaymentAmount) {
        this.interestRate = interestRate;
        this.repaymentAmount += repaymentAmount;
    }
}
