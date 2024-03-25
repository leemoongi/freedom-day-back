package com.side.freedomdaybackend.domain.loan.loanRepaymentMonthHistory;

import com.side.freedomdaybackend.domain.loan.Loan;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LoanRepaymentMonthHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    private int type; // 반환 타입
    private Long repaymentAmount; // 금액
    private LocalDateTime historyDate; // 상환일시

}
