package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.domain.loan.dto.LoanDto;
import com.side.freedomdaybackend.domain.loan.loanRepaymentMonthHistory.LoanRepaymentMonthHistory;
import com.side.freedomdaybackend.domain.member.Member;
import com.side.freedomdaybackend.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;
    private final LoanMapstruct loanMapstruct;

    public void myLoanList() {
        // 유저 id
        Member member = memberRepository.findById(1L).get();
        Long memberId = member.getId();

        // 내 대출 리스트 정보
        List<Loan> loanList = loanRepository.findByLoanList(memberId);
        List<LoanDto> loanDtoList = loanMapstruct.toLoanDtoList(loanList);

        LocalDate now = LocalDate.now();
        for (LoanDto loanDto : loanDtoList) {
            // 남은 납부일 계산
            int dayOfMonth = now.getDayOfMonth();
            LocalDate paymentDate = LocalDate.of(now.getYear(), now.getMonth(), loanDto.getPaymentDate());
            if (dayOfMonth > loanDto.getPaymentDate()) {
                paymentDate = paymentDate.plusMonths(1);
            }
            long d_day = ChronoUnit.DAYS.between(now, paymentDate);
            loanDto.setPaymentDDay((int) d_day);

            // 남은 원금 계산
            long principal = loanDto.getTotalPrincipal() - loanDto.getRepaymentAmount();
            loanDto.setPrincipal(principal);
        }

        int repaymentRate; // 상환률 = 전체대출원금,  전체남은원금 으로 백분율
        long totalPayment;
        long previousMonthPayment; // 지난달 총 납부액
        int loanCount = loanDtoList.size(); // 개수

        List<LoanRepaymentMonthHistory> byPreviousMonthPayment = loanRepository.findByPreviousMonthPayment(memberId);

    }
}
