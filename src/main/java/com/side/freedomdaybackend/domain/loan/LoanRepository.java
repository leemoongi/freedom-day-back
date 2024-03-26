package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>, LoanRepositoryCustom {


}
