package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.common.exception.CustomException;
import com.side.freedomdaybackend.common.exception.ErrorCode;
import com.side.freedomdaybackend.domain.loan.dto.*;
import com.side.freedomdaybackend.domain.loan.loanRepaymentMonthHistory.LoanRepaymentMonthHistory;
import com.side.freedomdaybackend.domain.loan.loanRepaymentMonthHistory.LoanRepaymentMonthHistoryRepository;
import com.side.freedomdaybackend.domain.member.Member;
import com.side.freedomdaybackend.mapper.LoanMapper;
import com.side.freedomdaybackend.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanService {

    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;
    private final LoanRepaymentMonthHistoryRepository loanRepaymentMonthHistoryRepository;
    private final LoanMapstruct loanMapstruct;
    private final LoanMapper loanMapper;

    public MyLoanInfoDto myLoanList(long memberId) {

        // 내 대출 리스트 정보
        List<LoanSimpleDto> loanSimpleDtoList = loanRepository.findByLoanList(memberId);

        int repaymentRate = 0; // 상환률 = 상환금액 / 대출원금 * 100
        double totalPayment = 0; // 대출원금
        double repaymentAmount = 0; // 상환금액
        double loanAmount = 0; // 남은 원금
        int loanCount = 0; // 개수
        Long previousMonthPayment = loanRepository.findByPreviousMonthPayment(memberId).orElse(0L); // 지난달 총 납부액

        LocalDate now = LocalDate.now();
        for (LoanSimpleDto loanSimpleDto : loanSimpleDtoList) {
            // 남은 납부일 계산 D-day
            int dayOfMonth = now.getDayOfMonth();
            LocalDate paymentDate = LocalDate.of(now.getYear(), now.getMonth(), loanSimpleDto.getPaymentDate());
            if (dayOfMonth > loanSimpleDto.getPaymentDate()) {
                paymentDate = paymentDate.plusMonths(1);
            }
            long d_day = ChronoUnit.DAYS.between(now, paymentDate);
            loanSimpleDto.setPaymentDDay((int) d_day);

            // 남은 원금 계산
            long principal = loanSimpleDto.getTotalPrincipal() - loanSimpleDto.getRepaymentAmount();
            loanSimpleDto.setOutstandingPrincipal(principal);

            totalPayment = totalPayment + loanSimpleDto.getTotalPrincipal();
            repaymentAmount = repaymentAmount + loanSimpleDto.getRepaymentAmount();
            loanSimpleDto.setPaymentPercentage((int) ((double) loanSimpleDto.getRepaymentAmount() / (double) loanSimpleDto.getTotalPrincipal() * 100)); //
        }

        repaymentRate = (int) (repaymentAmount / totalPayment * 100);
        loanCount = loanSimpleDtoList.size();

        // api 객체로 변경
        List<MyLoanInfoDto.LoanSimpleDto> loanSimpleDto = loanMapstruct.toLoanSimpleDto(loanSimpleDtoList);
        MyLoanInfoDto myLoanInfoDto = new MyLoanInfoDto(previousMonthPayment, repaymentRate, loanCount, loanSimpleDto);

        return myLoanInfoDto;
    }

    public LoanStatisticsDto statistics(long memberId) {
        LoanStatisticsDto.Tmp statisticsTmp = loanRepository.statistics(memberId); // 임시 객체
        LoanStatisticsDto loanStatisticsDto = loanMapstruct.toLoanStatisticsDto(statisticsTmp); // response 객체
        List<LoanStatisticsDto.LoanSimpleTmp> loanTmpList = loanRepository.loanSimple(memberId);

        // 총 남은 원금 = 대출 금액 - 대출 상환 금액
        loanStatisticsDto.setTotalRemainingPrincipal(loanStatisticsDto.getTotalPrincipal() - loanStatisticsDto.getTotalPrincipalRepayment());

        /* LoanList 상환 예정 */
        LocalDate now = LocalDate.now();
        List<LoanStatisticsDto.LoanSimple> loanSimpleList = new ArrayList<LoanStatisticsDto.LoanSimple>();
        for (LoanStatisticsDto.LoanSimpleTmp tmp : loanTmpList) {
            int paymentNumber = tmp.getPaymentDate(); // ex) 16
            int d_day = repaymentCountdown(paymentNumber, now);
            LocalDate paymentDate = getRepaymentDate(paymentNumber, now);

            LoanStatisticsDto.LoanSimple loanSimple = new LoanStatisticsDto.LoanSimple(
                    tmp.getName()
                    , tmp.getPurpose()
                    , d_day
                    , paymentDate
            );

            loanSimpleList.add(loanSimple);
        }

        /* RepaidLoan 상환 완료 */
        List<LoanStatisticsDto.RepaidLoan> repaidLoan = loanRepository.repaidLoan(memberId);

        /* RepaymentHistoryMonthList 월별 상환 기록 */
        List<LoanStatisticsDto.RepaymentHistoryMonth> rhmList = loanRepository.repaymentHistoryList(memberId);
        LocalDate originationDate = statisticsTmp.getOriginationDate();
        LocalDate expirationDate = statisticsTmp.getExpirationDate();

        YearMonth orYM = YearMonth.from(originationDate);
        YearMonth exYM = YearMonth.from(expirationDate);
        YearMonth nowYM = YearMonth.from(now);

        YearMonth ym = null; // 이 객체로 계산함

        // 종료날짜가 미래면 현재날짜로 세팅
        if (exYM.isAfter(nowYM)) ym = nowYM;
        else ym = exYM;

        long between = ChronoUnit.MONTHS.between(orYM, ym);

        Queue<LoanStatisticsDto.RepaymentHistoryMonth> queue = new LinkedList(rhmList);
        rhmList = new ArrayList<>(); // 담을 리스트 초기화

        LoanStatisticsDto.RepaymentHistoryMonth poll = queue.poll();

        YearMonth pollYM = null;
        if (poll != null) pollYM = YearMonth.from(poll.getHistoryDate());

        for (int i = 0; i < between; i++) {
            // 해당 달에 기록이 있음
            if (orYM.equals(pollYM)) {
                rhmList.add(poll);
                poll = queue.poll();

                if (!queue.isEmpty())
                    pollYM = YearMonth.from(poll.getHistoryDate());
                // 해당 달에 기록이 없음
            } else {
                rhmList.add(new LoanStatisticsDto.RepaymentHistoryMonth(LocalDate.of(orYM.getYear(),orYM.getMonth(),1), 0, 0, 0));
            }
            orYM = orYM.plusMonths(1);
        }

        /* RemainingPrincipal 대출 원금 비중 */
        List<LoanStatisticsDto.RemainingPrincipal> rpList = loanRepository.remainingPrincipal(memberId);
        long total = loanStatisticsDto.getTotalRemainingPrincipal();

        // 퍼센테이지 구하기
        for (LoanStatisticsDto.RemainingPrincipal rp : rpList) {
            long remainingPrincipal = rp.getRemainingPrincipal();
            double percentage = ((double) remainingPrincipal / total) * 100;
            rp.setPercentage((int) percentage);
        }

        loanStatisticsDto.setLoanList(loanSimpleList);
        loanStatisticsDto.setRepaidLoanList(repaidLoan);
        loanStatisticsDto.setRepaymentHistoryMonthList(rhmList);
        loanStatisticsDto.setRemainingPrincipalList(rpList);

        return loanStatisticsDto;
    }


    public void create(Member member, LoanCreateDto loanCreateDto) {
        char unit = loanCreateDto.getPeriodUnit();

        Loan loan = loanMapstruct.toLoan(loanCreateDto);
        loan.setCreateBefor(member);
        loanRepository.save(loan);
    }

    public LoanDetailResponseDto detail(Long memberId, LoanDetailRequestDto loanDetailRequestDto) {
        Long loanId = loanDetailRequestDto.getLoanId();
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new CustomException(ErrorCode.LOAN_NOT_FOUND));

        // 해당유저의 대출이 아닐경우
        if (!loan.getMember().getId().equals(memberId))
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR); // TODO) 에러코드 추가

        // response 객체
        LoanDetailResponseDto loanDetailResponseDto = loanMapstruct.toLoanDetailResponseDto(loan);

        // 월별 상세 정보
        List<LoanDetailResponseDto.RepaymentHistoryMonth> rhmList = loanRepository.detailRepaymentMonthHistory(loanId);

        LocalDate now = LocalDate.now();
        LocalDate originationDate = loan.getOriginationDate();
        LocalDate expirationDate = loan.getExpirationDate();

        YearMonth orYM = YearMonth.from(originationDate);
        YearMonth exYM = YearMonth.from(expirationDate);
        YearMonth nowYM = YearMonth.from(now);

        YearMonth ym = null; // 이 객체로 계산함

        // 종료날짜가 미래면 현재날짜로 세팅
        if (exYM.isAfter(nowYM)) ym = nowYM;
        else ym = exYM;

        long between = ChronoUnit.MONTHS.between(orYM, ym);

        Queue<LoanDetailResponseDto.RepaymentHistoryMonth> queue = new LinkedList(rhmList);
        rhmList = new ArrayList<>(); // 담을 리스트 초기화

        LoanDetailResponseDto.RepaymentHistoryMonth poll = queue.poll();

        YearMonth pollYM = null;
        if (poll != null) pollYM = YearMonth.from(poll.getHistoryDate());

        for (int i = 0; i < between; i++) {
            // 해당 달에 기록이 있음
            if (orYM.equals(pollYM)) {
                rhmList.add(poll);
                poll = queue.poll();

                if (!queue.isEmpty())
                    pollYM = YearMonth.from(poll.getHistoryDate());
                // 해당 달에 기록이 없음
            } else {
                rhmList.add(new LoanDetailResponseDto.RepaymentHistoryMonth(LocalDate.of(orYM.getYear(),orYM.getMonth(),1), 0, 0, 0));
            }
            orYM = orYM.plusMonths(1);
        }

        loanDetailResponseDto.setRepaymentHistoryMonthList(rhmList);

        return loanDetailResponseDto;

    }

    public void addRepaymentDetails(Long memberId, LoanAddRepaymentDetailDto lardDto) {
        // 대출 존재 확인
        long loanId = lardDto.getLoanId();
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));

        // 사용자 일치 여부 확인
        Member member = loan.getMember();
        if (!member.getId().equals(memberId)) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        };

        double interestRates = lardDto.getInterestRates();
        long repaymentAmount2 = lardDto.getRepaymentAmount2(); // 납입원금
        long repaymentAmount3 = lardDto.getRepaymentAmount3(); // 중도상환

        // 대출 상환내역 추가
        loan.addRepaymentAmount(interestRates, repaymentAmount2 + repaymentAmount3);

        // TODO) 히스토리 칼럼에 조건 검사

        // entity
        LocalDate now = LocalDate.now();
        LoanRepaymentMonthHistory entity = LoanRepaymentMonthHistory.builder()
                .loan(loan)
                .interestRates(lardDto.getInterestRates())
                .repaymentAmount1(lardDto.getRepaymentAmount1())
                .repaymentAmount2(lardDto.getRepaymentAmount2())
                .repaymentAmount3(lardDto.getRepaymentAmount3())
                .historyDate(lardDto.getHistoryDate())
                .build();

        loanRepaymentMonthHistoryRepository.save(entity);
    }


    // 상환일로 D-day 계산
    public int repaymentCountdown(int paymentNumber, LocalDate now) {
        // 상환 날짜
        LocalDate date = LocalDate.of(now.getYear(), now.getMonth(), paymentNumber);

        // 상환날자가 지났을 경우, 한달 더함
        if (isPaidThisMonth(paymentNumber, now)) {
            date = date.plusMonths(1);
        }

        return (int) ChronoUnit.DAYS.between(now, date);
    }


     // 상환일로 D-day 계산
    public LocalDate getRepaymentDate(int paymentNumber, LocalDate now) {
        // 상환 날짜
        LocalDate date = LocalDate.of(now.getYear(), now.getMonth(), paymentNumber);

        // 상환날자가 지났을 경우, 한달 더함
        if (isPaidThisMonth(paymentNumber, now)) {
            date = date.plusMonths(1);
        }
        return date;
    }


    public boolean isPaidThisMonth(int paymentNumber, LocalDate now) {
        int currentDay = now.getDayOfMonth();

        // 현재 날짜가 상환날짜보다 크다 => 이미 지급했음
        if (currentDay > paymentNumber) return true;
        return false;
    }

}
