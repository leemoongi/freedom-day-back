package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.common.response.ApiResponse;
import com.side.freedomdaybackend.common.util.AuthUtil;
import com.side.freedomdaybackend.domain.loan.dto.LoanCreateDto;
import com.side.freedomdaybackend.domain.loan.dto.MyLoanInfoDto;
import com.side.freedomdaybackend.domain.loan.dto.LoanStatisticsDto;
import com.side.freedomdaybackend.domain.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loan")
public class LoanController {

    public final LoanService loanService;
    public final AuthUtil authUtil;

    @GetMapping("/test")
    public void test() {
//        loanService.myLoanList();
    }

    @GetMapping("/user-loan-info")
    public ApiResponse userLoanInfo(HttpServletRequest request) {
        Long memberId = authUtil.checkAuthReturnId(request);

        MyLoanInfoDto myLoanInfoDto = loanService.myLoanList(memberId);
        return new ApiResponse<>(myLoanInfoDto);
    }

    @GetMapping("/loan-statistics")
    public ApiResponse loanStatistics(HttpServletRequest request) {
        Long memberId = authUtil.checkAuthReturnId(request);

        LoanStatisticsDto loanStatisticsDto = loanService.statistics(memberId);
        return new ApiResponse<>(loanStatisticsDto);
    }

    @GetMapping("/create")
    public ApiResponse create(HttpServletRequest request, @RequestBody LoanCreateDto loanCreateDto) {
        Member member = authUtil.checkAuthReturnMember(request);

        loanService.create(member, loanCreateDto);
        return new ApiResponse<>();
    }

}
