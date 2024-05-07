package com.side.freedomdaybackend.domain.loan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.freedomdaybackend.common.constants.Constants;
import com.side.freedomdaybackend.common.util.AuthUtil;
import com.side.freedomdaybackend.domain.RestDocsTest;
import com.side.freedomdaybackend.domain.loan.dto.MyLoanInfoDto;
import com.side.freedomdaybackend.domain.loan.dto.StatisticsDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
class LoanControllerTest extends RestDocsTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestDocumentationResultHandler restDocs;

    @MockBean
    private LoanService loanService;

    @MockBean
    private AuthUtil authUtil;


    @DisplayName("내 대출 정보")
    @Test
    void userLoanInfo() throws Exception {
        // given
        List<MyLoanInfoDto.LoanSimpleDto> loanDtoList = new ArrayList<>();
        loanDtoList.add(
                new MyLoanInfoDto.LoanSimpleDto(
                        1L,
                        "토스뱅크 신용대출",
                        "생활비",
                        20,
                        800000,
                        LocalDateTime.of(2024, Month.DECEMBER, 3, 22, 0),
                        16,
                        9));
        loanDtoList.add(
                new MyLoanInfoDto.LoanSimpleDto(
                        1L,
                        "카카오뱅크 신용대출",
                        "생활비",
                        20,
                        120000,
                        LocalDateTime.of(2024, Month.DECEMBER, 3, 22, 0),
                        16,
                        0));
        MyLoanInfoDto myLoanInfoDto = new MyLoanInfoDto(50000L, 8, 2, loanDtoList);

        // when
        when(authUtil.checkAuth(any())).thenReturn(1L);
        when(loanService.myLoanList(anyLong())).thenReturn(myLoanInfoDto);

        ResultActions perform = mockMvc.perform(get("/loan/user-loan-info")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())

                // spring rest docs
                .andDo(
                        restDocs.document(
                                responseFields(
                                        beneathPath("response").withSubsectionId("response"),
                                        fieldWithPath("previousMonthPayment").type(JsonFieldType.NUMBER).description("지난달 총 납부금액"),
                                        fieldWithPath("repaymentRate").type(JsonFieldType.NUMBER).description("상활률"),
                                        fieldWithPath("loanCount").type(JsonFieldType.NUMBER).description("대출 개수"),
                                        fieldWithPath("loanSimpleDtoList[].id").type(JsonFieldType.NUMBER).description("pk"),
                                        fieldWithPath("loanSimpleDtoList[].name").type(JsonFieldType.STRING).description("대출이름"),
                                        fieldWithPath("loanSimpleDtoList[].purpose").type(JsonFieldType.STRING).description("용도"),
                                        fieldWithPath("loanSimpleDtoList[].paymentDDay").type(JsonFieldType.NUMBER).description("남은 납부일"),
                                        fieldWithPath("loanSimpleDtoList[].outstandingPrincipal").type(JsonFieldType.NUMBER).description("남은 원금"),
                                        fieldWithPath("loanSimpleDtoList[].expirationDate").type(JsonFieldType.STRING).description("만기일"),
                                        fieldWithPath("loanSimpleDtoList[].paymentDate").type(JsonFieldType.NUMBER).description("납부일"),
                                        fieldWithPath("loanSimpleDtoList[].paymentPercentage").type(JsonFieldType.NUMBER).description("납부 진행률")
                                )
                        ));
    }

    @DisplayName("대출 통계")
    @Test
    void loanStatistics() throws Exception {

        // given
        StatisticsDto statisticsDto = new StatisticsDto();
        statisticsDto.setTotalPrincipal(30000000);
        statisticsDto.setTotalPrincipalRepayment(10000000);
        statisticsDto.setTotalRemainingPrincipal(20000000);

        List<StatisticsDto.LoanSimple> loanSimpleList = new ArrayList<>();
         loanSimpleList.add(
                new StatisticsDto.LoanSimple(
                        "카카오뱅크 청년 전월세",
                        "주택자금",
                        9,
                        LocalDate.of(2024, 5, 16)));
        loanSimpleList.add(
                new StatisticsDto.LoanSimple(
                        "카카오뱅크 신용대출",
                        "생활비",
                        9,
                        LocalDate.of(2024, 5, 16))
        );

        List<StatisticsDto.RepaidLoan> repaidLoanList = new ArrayList<>();
        repaidLoanList.add(
                new StatisticsDto.RepaidLoan(
                        "우리은행 청년 버팀목"
                        , "주택자금"
                        , 200000000
                )
        );

        List<StatisticsDto.RepaymentHistoryMonth> rhmList = new ArrayList<>();
        rhmList.add(
                new StatisticsDto.RepaymentHistoryMonth(
                        "2024-02"
                        , 10000000
                        , 20
                        , 50000000
                )
        );
        rhmList.add(
                new StatisticsDto.RepaymentHistoryMonth(
                        "2024-03"
                        , 10000000
                        , 10
                        , 0
                )
        );
        rhmList.add(
                new StatisticsDto.RepaymentHistoryMonth(
                        "2024-02"
                        , 10000000
                        , 10
                        , 20000000
                )
        );

        List<StatisticsDto.RemainingPrincipal> rpList = new ArrayList<>();
        rpList.add(
                new StatisticsDto.RemainingPrincipal(
                        "주택자금"
                        , 19000000
                        , 95
                ));
      rpList.add(
                new StatisticsDto.RemainingPrincipal(
                        "생활비"
                        , 1000000
                        , 5
                ));

        statisticsDto.setLoanList(loanSimpleList);
        statisticsDto.setRepaidLoanList(repaidLoanList);
        statisticsDto.setRepaymentHistoryMonthList(rhmList);
        statisticsDto.setRemainingPrincipalList(rpList);

        // when
        when(authUtil.checkAuth(any())).thenReturn(2L);
        when(loanService.statistics(anyLong())).thenReturn(statisticsDto);

        ResultActions perform = mockMvc.perform(get("/loan/loan-statistics")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())

                // spring rest docs
                .andDo(
                        restDocs.document(
                                responseFields(
                                        beneathPath("response").withSubsectionId("response"),
                                        fieldWithPath("totalPrincipal").type(JsonFieldType.NUMBER).description("총 대출 금액"),
                                        fieldWithPath("totalPrincipalRepayment").type(JsonFieldType.NUMBER).description("총 상환 원금"),

                                        fieldWithPath("loanList[].name").type(JsonFieldType.STRING).description("진행중인 대출 - 대출 이름"),
                                        fieldWithPath("loanList[].purpose").type(JsonFieldType.STRING).description("진행중인 대출 - 용도"),
                                        fieldWithPath("loanList[].paymentDDay").type(JsonFieldType.NUMBER).description("진행중인 대출 - 남은 납부일 D-Day"),
                                        fieldWithPath("loanList[].paymentDate").type(JsonFieldType.STRING).description("진행중인 대출 - 납부 예정일"),

                                        fieldWithPath("repaidLoanList[].name").type(JsonFieldType.STRING).description("종료된 대출 - 대출 이름"),
                                        fieldWithPath("repaidLoanList[].purpose").type(JsonFieldType.STRING).description("종료된 대출 - 용도"),
                                        fieldWithPath("repaidLoanList[].repaymentAmount").type(JsonFieldType.NUMBER).description("종료된 대출 - 납부한 금액"),

                                        fieldWithPath("repaymentHistoryMonthList[].historyDate").type(JsonFieldType.STRING).description("월 상환 기록 - 등록 월 ex) yyyy-mm"),
                                        fieldWithPath("repaymentHistoryMonthList[].repaymentAmount1").type(JsonFieldType.NUMBER).description("월 상환 기록 - 원금 상환"),
                                        fieldWithPath("repaymentHistoryMonthList[].repaymentAmount2").type(JsonFieldType.NUMBER).description("월 상환 기록 - 이자"),
                                        fieldWithPath("repaymentHistoryMonthList[].repaymentAmount3").type(JsonFieldType.NUMBER).description("월 상환 기록 - 중도 상환"),

                                        fieldWithPath("totalRemainingPrincipal").type(JsonFieldType.NUMBER).description("남은 총 남은원금"),

                                        fieldWithPath("remainingPrincipalList[].purpose").type(JsonFieldType.STRING).description("원금 퍼센트 - 대출 이름"),
                                        fieldWithPath("remainingPrincipalList[].remainingPrincipal").type(JsonFieldType.NUMBER).description("원금 퍼센트 - 남은 금액"),
                                        fieldWithPath("remainingPrincipalList[].percentage").type(JsonFieldType.NUMBER).description("원금 퍼센트 - 백분율")
                                )
                        ));

    }

}