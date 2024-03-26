package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.domain.loan.loanRepaymentMonthHistory.LoanRepaymentMonthHistory;
import com.side.freedomdaybackend.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    private int interestRate; // 연 이자율
    private int loanPeriod; // 연 이자율
    private Boolean variableRate; // 변동금리 여부
    private LocalDateTime originationDate; // 대출 기간  TODO) 기간에 대한 정보를 어떻게 저장할지 결졍해야함
    private LocalDateTime repaymentDate; // 시작 일시
    private int paymentDate; // 납부일  매월 15일에 납부 -> 15

}
