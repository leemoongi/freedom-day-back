package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.domain.loan.loanRepaymentMonthHistory.LoanRepaymentMonthHistory;
import com.side.freedomdaybackend.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
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
    private String totalPrincipal; // 총 원금
    private String repaymentAmount; // 상환 완료 금액
    private String interestRate; // 연 이자율
    private Boolean loanPeriod; // 변동금리 여부
    private Boolean originationDate; // 대출 기간
    private Boolean repaymentDate; // 시작 일시
    private Boolean paymentDate; // 상환 일시

}
