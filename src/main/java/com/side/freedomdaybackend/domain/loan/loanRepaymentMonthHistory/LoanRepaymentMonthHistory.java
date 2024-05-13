package com.side.freedomdaybackend.domain.loan.loanRepaymentMonthHistory;

import com.side.freedomdaybackend.domain.loan.Loan;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    private double interestRates;
    private Long repaymentAmount1; // 원금
    private Long repaymentAmount2; // 이자
    private Long repaymentAmount3; // 중도상환
    private LocalDate historyDate; // 상환일시

}
