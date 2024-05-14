package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.common.response.ApiResponse;
import com.side.freedomdaybackend.common.util.AuthUtil;
import com.side.freedomdaybackend.domain.loan.dto.*;
import com.side.freedomdaybackend.domain.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

        MyLoanInfoDto dto = loanService.myLoanList(memberId);
        return new ApiResponse<>(dto);
    }

    @GetMapping("/loan-statistics")
    public ApiResponse loanStatistics(HttpServletRequest request) {
        Long memberId = authUtil.checkAuthReturnId(request);

        LoanStatisticsDto dto = loanService.statistics(memberId);
        return new ApiResponse<>(dto);
    }

    @PostMapping("/create")
    public ApiResponse create(HttpServletRequest request, @RequestBody LoanCreateDto loanCreateDto) {
        Member member = authUtil.checkAuthReturnMember(request);

        loanService.create(member, loanCreateDto);
        return new ApiResponse<>();
    }

    @GetMapping("/detail")
    public ApiResponse detail(HttpServletRequest request, @RequestBody LoanDetailRequestDto loanDetailRequestDto) {
        Long memberId = authUtil.checkAuthReturnId(request);

        LoanDetailResponseDto dto = loanService.detail(memberId, loanDetailRequestDto);
        return new ApiResponse<>(dto);
    }

    @PostMapping("/add-repayment-details")
    public ApiResponse addRepaymentDetails(HttpServletRequest request, @RequestBody LoanAddRepaymentDetailDto lardDto) {
        Long memberId = authUtil.checkAuthReturnId(request);

        loanService.addRepaymentDetails(memberId, lardDto);
        return new ApiResponse<>();
    }

}
