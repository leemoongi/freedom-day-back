package com.side.freedomdaybackend.domain.loan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.freedomdaybackend.common.util.AuthUtil;
import com.side.freedomdaybackend.domain.RestDocsTest;
import com.side.freedomdaybackend.domain.loan.dto.*;
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
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                        LocalDate.of(2024, Month.DECEMBER, 3),
                        16,
                        9));
        loanDtoList.add(
                new MyLoanInfoDto.LoanSimpleDto(
                        1L,
                        "카카오뱅크 신용대출",
                        "생활비",
                        20,
                        120000,
                        LocalDate.of(2024, Month.DECEMBER, 3),
                        16,
                        0));
        MyLoanInfoDto myLoanInfoDto = new MyLoanInfoDto(50000L, 8, 2, loanDtoList);

        // when
        when(authUtil.checkAuthReturnId(any())).thenReturn(1L);
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
        LoanStatisticsDto loanStatisticsDto = new LoanStatisticsDto();
        loanStatisticsDto.setTotalPrincipal(30000000);
        loanStatisticsDto.setTotalPrincipalRepayment(10000000);
        loanStatisticsDto.setTotalRemainingPrincipal(20000000);

        List<LoanStatisticsDto.LoanSimple> loanSimpleList = new ArrayList<>();
         loanSimpleList.add(
                new LoanStatisticsDto.LoanSimple(
                        "카카오뱅크 청년 전월세",
                        "주택자금",
                        9,
                        LocalDate.of(2024, 5, 16)));
        loanSimpleList.add(
                new LoanStatisticsDto.LoanSimple(
                        "카카오뱅크 신용대출",
                        "생활비",
                        9,
                        LocalDate.of(2024, 5, 16))
        );

        List<LoanStatisticsDto.RepaidLoan> repaidLoanList = new ArrayList<>();
        repaidLoanList.add(
                new LoanStatisticsDto.RepaidLoan(
                        "우리은행 청년 버팀목"
                        , "주택자금"
                        , 200000000
                )
        );

        List<LoanStatisticsDto.RepaymentHistoryMonth> rhmList = new ArrayList<>();
        rhmList.add(
                new LoanStatisticsDto.RepaymentHistoryMonth(
                         LocalDate.of(2024,Month.FEBRUARY,1)
                        , 10000000
                        , 20
                        , 50000000
                )
        );
        rhmList.add(
                new LoanStatisticsDto.RepaymentHistoryMonth(
                        LocalDate.of(2024,Month.MARCH,1)
                        , 10000000
                        , 10
                        , 0
                )
        );
        rhmList.add(
                new LoanStatisticsDto.RepaymentHistoryMonth(
                         LocalDate.of(2024,Month.FEBRUARY,1)
                        , 10000000
                        , 10
                        , 20000000
                )
        );

        List<LoanStatisticsDto.RemainingPrincipal> rpList = new ArrayList<>();
        rpList.add(
                new LoanStatisticsDto.RemainingPrincipal(
                        "주택자금"
                        , 19000000
                        , 95
                ));
      rpList.add(
                new LoanStatisticsDto.RemainingPrincipal(
                        "생활비"
                        , 1000000
                        , 5
                ));

        loanStatisticsDto.setLoanList(loanSimpleList);
        loanStatisticsDto.setRepaidLoanList(repaidLoanList);
        loanStatisticsDto.setRepaymentHistoryMonthList(rhmList);
        loanStatisticsDto.setRemainingPrincipalList(rpList);

        // when
        when(authUtil.checkAuthReturnId(any())).thenReturn(2L);
        when(loanService.statistics(anyLong())).thenReturn(loanStatisticsDto);

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


    @DisplayName("대출 생성")
    @Test
    void create() throws Exception {
        // given
        LoanCreateDto loanCreateDto = new LoanCreateDto(
                "카카오 청년전월세"
                ,"주택자금"
                ,""
                ,100000000
                ,0
                ,4.5
                ,false
                ,24
                ,LocalDate.of(2024,Month.JANUARY,1)
                ,LocalDate.of(2025,Month.DECEMBER,31)
                ,1
                ,'M'
                ,RepaymentMethod.BULLET_REPAYMENT
        );

        // when
        when(authUtil.checkAuthReturnId(any())).thenReturn(2L);

        ResultActions perform = mockMvc.perform(post("/loan/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loanCreateDto)));

        // then
        perform.andExpect(status().isOk())

                // spring rest docs
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("대출 이름"),
                                fieldWithPath("purpose").type(JsonFieldType.STRING).description("대출 목적"),
                                fieldWithPath("bankCode").type(JsonFieldType.STRING).description("은행"),
                                fieldWithPath("totalPrincipal").type(JsonFieldType.NUMBER).description("총 원금"),
                                fieldWithPath("repaymentAmount").type(JsonFieldType.NUMBER).description("상환 완료 금액"),
                                fieldWithPath("interestRate").type(JsonFieldType.NUMBER).description("연 이자율"),
                                fieldWithPath("variableRate").type(JsonFieldType.BOOLEAN).description("변동금리여부"),
                                fieldWithPath("loanPeriod").type(JsonFieldType.NUMBER).description("대출 기간"),
                                fieldWithPath("originationDate").type(JsonFieldType.STRING).description("시작 일시"),
                                fieldWithPath("expirationDate").type(JsonFieldType.STRING).description("상환 일시"),
                                fieldWithPath("paymentDate").type(JsonFieldType.NUMBER).description("납부일 매월 15일에 납부 -> 15"),
                                fieldWithPath("periodUnit").type(JsonFieldType.STRING).description("대출 기간 단위  월:M 일:D"),
                                fieldWithPath("repaymentMethod").type(JsonFieldType.STRING).description("상환 방법 BR:만기일시 / EPI:월리금균등 / EP:원금균등 "))));

    }


    @DisplayName("대출 상세")
    @Test
    void detail() throws Exception {
        // given
        LoanDetailRequestDto loanDetailRequestDto = new LoanDetailRequestDto(2L);
        LoanDetailResponseDto loanDetailResponseDto = new LoanDetailResponseDto(
                "우리은행 청년버팀목"
                , "주택자금"
                , "TEST"
                , 100000000
                , 100000000
                , null
                , 200000000
                , LocalDate.of(2024, Month.APRIL, 12)
                , 0
                , 2.1
        );

        List<LoanDetailResponseDto.RepaymentHistoryMonth> list = new ArrayList<>();
        LoanDetailResponseDto.RepaymentHistoryMonth repaymentHistoryMonth = new LoanDetailResponseDto.RepaymentHistoryMonth(
                LocalDate.of(2024, Month.APRIL, 1)
                , 2.1
                , 1000000
                , 1000000
                , 0
        );
        list.add(repaymentHistoryMonth);

        loanDetailResponseDto.setRepaymentHistoryMonthList(list);


        // when
        when(authUtil.checkAuthReturnId(any())).thenReturn(2L);
        when(loanService.detail(any(), any())).thenReturn(loanDetailResponseDto);

        ResultActions perform = mockMvc.perform(get("/loan/detail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loanDetailRequestDto)));

        // then
        perform.andExpect(status().isOk())

                // spring rest docs
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("loanId").type(JsonFieldType.NUMBER).description("대출 pk")
                        ),
                        responseFields(
                                beneathPath("response").withSubsectionId("response"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("purpose").type(JsonFieldType.STRING).description("목적"),
                                fieldWithPath("bankCode").type(JsonFieldType.STRING).description("은행 코드"),
                                fieldWithPath("outstandingPrincipal").type(JsonFieldType.NUMBER).description("남은 원금"),
                                fieldWithPath("repaymentAmount").type(JsonFieldType.NUMBER).description("상환 완료 금액"),
                                fieldWithPath("repaymentHistoryMonthList[].historyDate").type(JsonFieldType.STRING).description("상환 내역 리스트 - 등록 월"),
                                fieldWithPath("repaymentHistoryMonthList[].interestRate").type(JsonFieldType.NUMBER).description("상환 내역 리스트 - 이자율"),
                                fieldWithPath("repaymentHistoryMonthList[].repaymentAmount1").type(JsonFieldType.NUMBER).description("상환 내역 리스트 - 원금"),
                                fieldWithPath("repaymentHistoryMonthList[].repaymentAmount2").type(JsonFieldType.NUMBER).description("상환 내역 리스트 - 이자"),
                                fieldWithPath("repaymentHistoryMonthList[].repaymentAmount3").type(JsonFieldType.NUMBER).description("상환 내역 리스트 - 중도상환"),
                                fieldWithPath("totalPrincipal").type(JsonFieldType.NUMBER).description("대출 금액"),
                                fieldWithPath("originationDate").type(JsonFieldType.STRING).description("대출 실행일"),
                                fieldWithPath("loanPeriod").type(JsonFieldType.NUMBER).description("대출 기간"),
                                fieldWithPath("interestRate").type(JsonFieldType.NUMBER).description("이자율")



                        )
                ));

    }



    @DisplayName("대출 상세 추가")
    @Test
    void addRepaymentDetails() throws Exception {
        // given
        LoanAddRepaymentDetailDto lardDto = new LoanAddRepaymentDetailDto(
                1L
                ,3.4
                ,10000
                ,10000
                ,10000
                ,LocalDate.of(2024,05,11)
        );

        // when
        when(authUtil.checkAuthReturnId(any())).thenReturn(2L);

        ResultActions perform = mockMvc.perform(post("/loan/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lardDto)));

        // then
        perform.andExpect(status().isOk())

                // spring rest docs
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("loanId").type(JsonFieldType.NUMBER).description("대출"),
                                fieldWithPath("interestRates").type(JsonFieldType.NUMBER).description("금리"),
                                fieldWithPath("repaymentAmount1").type(JsonFieldType.NUMBER).description("납입이자"),
                                fieldWithPath("repaymentAmount2").type(JsonFieldType.NUMBER).description("납입원금"),
                                fieldWithPath("repaymentAmount3").type(JsonFieldType.NUMBER).description("중도상환"),
                                fieldWithPath("historyDate").type(JsonFieldType.STRING).description("상환날짜")
                        )));

    }



}