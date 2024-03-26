package com.side.freedomdaybackend.domain.loan.loanRepaymentMonthHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepaymentMonthHistoryRepository extends JpaRepository<LoanRepaymentMonthHistory, Long> {



}
