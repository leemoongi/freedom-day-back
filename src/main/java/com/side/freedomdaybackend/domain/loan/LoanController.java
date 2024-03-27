package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.common.response.ApiResponse;
import com.side.freedomdaybackend.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loan")
public class LoanController {

    public final LoanService loanService;

    @GetMapping("/test")
    public void test() {
        loanService.myLoanList();
    }

    @GetMapping("/user-loan-info")
    public void userLoanInfo() {
        loanService.myLoanList();
    }

}
