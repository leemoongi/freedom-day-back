package com.side.freedomdaybackend.domain.loan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.freedomdaybackend.common.constants.Constants;
import com.side.freedomdaybackend.common.util.AuthUtil;
import com.side.freedomdaybackend.domain.RestDocsTest;
import com.side.freedomdaybackend.domain.loan.dto.MyLoanInfoDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
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
                        "토스뱅크 신용대출",
                        "생활비",
                        20,
                        800000,
                        LocalDateTime.of(2024, Month.DECEMBER, 3, 22, 0),
                        16,
                        9));
        loanDtoList.add(
                new MyLoanInfoDto.LoanSimpleDto(
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

}