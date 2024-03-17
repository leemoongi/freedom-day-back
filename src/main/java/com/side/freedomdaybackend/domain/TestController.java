package com.side.freedomdaybackend.domain;

import com.side.freedomdaybackend.common.exception.CustomException;
import com.side.freedomdaybackend.common.exception.ErrorCode;
import com.side.freedomdaybackend.common.response.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public ApiResponse<String> getMappingTest() {
        String message = "테스트 성공";
        return new ApiResponse<String>(message);
    }

    @GetMapping("graph1")
    public ApiResponse<ArrayList<TestDto>> graph() {
        ArrayList<TestDto> list = new ArrayList<>();
        list.add(new TestDto("생활비", 9900000));
        list.add(new TestDto("학자금", 19800000));
        list.add(new TestDto("자동차", 9900000));
        list.add(new TestDto("기타", 59400000));
        list.add(new TestDto("주택자금", 99000000));
        return new ApiResponse<ArrayList<TestDto>>(list);
    }

    @GetMapping("/user-loan-info")
    public ApiResponse userLoanInfo() {
        List<LoanDto> loanDtoList = new ArrayList<>();
        loanDtoList.add(
                new LoanDto(
                        "토스뱅크 신용대출",
                        "생활비",
                        16,
                        19886317,
                        LocalDateTime.of(2024, Month.DECEMBER, 04, 0, 0),
                        15));
        loanDtoList.add(
                new LoanDto(
                        "카카오뱅크 신용대출",
                        "생활비",
                        16,
                        19886317,
                        LocalDateTime.of(2024, Month.DECEMBER, 04, 0, 0),
                        15));

        UserLoanDto userLoanDto = new UserLoanDto("이문기", 5800000, 90000000, 2, loanDtoList);
        return new ApiResponse(userLoanDto);

    }

    @GetMapping("/error")
    public void errorTest() {
        String message = "테스트 성공";
        throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
    }


}


/**
 * 이름 (홍길동)
 * 지난달 총 납부액 숫자로
 * 총 납부해야 할 금액
 * 나의 대출 개수
 * details관련 내용
     * 나의대출1(토스뱅크 신용대출)
     * 용도(생활비)
     * 납부일까지 (D-16) -- 숫자로?
     * 나의대출금액1(19,886,317)
     * 만기일(2024.12.04)
     * 납부일(매달 15일)
 */
@Data
@AllArgsConstructor
class UserLoanDto {
    String userName; // 유저 이름
    long previousMonthPayment; // 지난달 납부액
    long totalPayment; // 총 납부 금액
    int loanCount; // 대출 개수
    List<LoanDto> loanDtoList;

}

@Data
@AllArgsConstructor
class LoanDto {
    String loanName; // 대출이름
    String purpose; // 용도
    long paymentDDay; // 남은 납부일
    int loanAmount; // 대출 금액
    LocalDateTime loanExpirationDate;// 만기일
    int paymentDueDay;// 납부일
}

@Data
@AllArgsConstructor
class TestDto {
    String name;
    long amount;
}
