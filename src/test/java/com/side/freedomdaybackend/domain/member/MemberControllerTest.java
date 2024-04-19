package com.side.freedomdaybackend.domain.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.freedomdaybackend.common.constants.Constants;
import com.side.freedomdaybackend.domain.RestDocsTest;
import com.side.freedomdaybackend.domain.member.dto.SignInRequestDto;
import com.side.freedomdaybackend.domain.member.dto.SignInResponseDto;
import com.side.freedomdaybackend.domain.member.dto.SignUpRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MemberControllerTest extends RestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @DisplayName("로그인")
    @Transactional
    @Test
    void signIn() throws Exception {
        // given
        SignInRequestDto signInRequestDto = new SignInRequestDto("test1234@naver.com", "test1234@");
        SignInResponseDto signInResponseDto = new SignInResponseDto();

        // when
        mockMvc.perform(post("/member/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequestDto)))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(cookie().exists(Constants.ACCESS_TOKEN))
                .andExpect(cookie().exists(Constants.REFRESH_TOKEN))

                // spring rest docs
                .andDo(
                        document("/member/sign-in",
                                Preprocessors.preprocessRequest(prettyPrint()),
                                Preprocessors.preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("멤버 이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")),
                                responseFields(
                                        fieldWithPath("id").description("멤버 PK")
                                )));
    }

    @DisplayName("회원가입")
    @Transactional
    @Test
    void signUp() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("newTestTest@naver.com", "test1234@", "testNickName", 'M', "19880101");

        // when
        mockMvc.perform(post("/member/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestDto)))

                // then
                .andExpect(status().isOk())

                // spring rest docs
                .andDo(document("/member/sign-up",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("sex").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).description("생년월일")
                        )
                ));
    }





    @Test
    void emailAuthentication() {
    }

    @Test
    void memberList() {
    }

    @Test
    void queryDslTest() {
    }
}