package com.side.freedomdaybackend.domain.loan.loanRepaymentMonthHistory;

import com.side.freedomdaybackend.domain.loan.Loan;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LoanRepaymentMonthHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    private int type; // 반환 타입   1:원금  2:이자  3:중도상환
    private Long repaymentAmount; // 금액
    private LocalDateTime historyDate; // 상환일시

}
