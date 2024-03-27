package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.domain.loan.dto.LoanDto;
import com.side.freedomdaybackend.domain.loan.dto.MyLoanInfoDto;
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

    public MyLoanInfoDto myLoanList() {
        // 유저 id
        Member member = memberRepository.findById(1L).get();
        Long memberId = member.getId();

        // 내 대출 리스트 정보
        List<Loan> loanList = loanRepository.findByLoanList(memberId);
        List<LoanDto> loanDtoList = loanMapstruct.toLoanDtoList(loanList);

        int repaymentRate = 0; // 상환률 = 상환금액 / 대출원금 * 100
        double totalPayment = 0; // 대출원금
        double repaymentAmount = 0; // 상환금액
        double loanAmount = 0; // 남은 원금
        int loanCount = 0; // 개수
        long previousMonthPayment = loanRepository.findByPreviousMonthPayment(memberId);; // 지난달 총 납부액

        LocalDate now = LocalDate.now();
        for (LoanDto loanDto : loanDtoList) {
            // 남은 납부일 계산 D-day
            int dayOfMonth = now.getDayOfMonth();
            LocalDate paymentDate = LocalDate.of(now.getYear(), now.getMonth(), loanDto.getPaymentDate());
            if (dayOfMonth > loanDto.getPaymentDate()) {
                paymentDate = paymentDate.plusMonths(1);
            }
            long d_day = ChronoUnit.DAYS.between(now, paymentDate);
            loanDto.setPaymentDDay((int) d_day);

            // 남은 원금 계산
            long principal = loanDto.getTotalPrincipal() - loanDto.getRepaymentAmount();
            loanDto.setOutstandingPrincipal(principal);

            totalPayment = totalPayment + loanDto.getTotalPrincipal();
            repaymentAmount = repaymentAmount + loanDto.getRepaymentAmount();
        }

        repaymentRate = (int) (repaymentAmount / totalPayment * 100);
        loanCount = loanDtoList.size();

        // api 객체
        List<MyLoanInfoDto.LoanSimpleDto> loanSimpleDto = loanMapstruct.toLoanSimpleDto(loanDtoList);
        MyLoanInfoDto myLoanInfoDto = new MyLoanInfoDto(previousMonthPayment, repaymentRate, loanCount, loanSimpleDto);

        return myLoanInfoDto;
    }
}
