package com.side.freedomdaybackend.domain.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.freedomdaybackend.common.constants.Constants;
import com.side.freedomdaybackend.common.util.*;
import com.side.freedomdaybackend.domain.RestDocsTest;
import com.side.freedomdaybackend.domain.member.dto.EmailAuthenticationDto;
import com.side.freedomdaybackend.domain.member.dto.SignInRequestDto;
import com.side.freedomdaybackend.domain.member.dto.SignInResponseDto;
import com.side.freedomdaybackend.domain.member.dto.SignUpRequestDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
class MemberControllerTest extends RestDocsTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestDocumentationResultHandler restDocs;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CookieUtil cookieUtil;

    @MockBean
    private MemberService memberService;

    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private AuthUtil authUtil;

    @MockBean
    private EmailUtil emailUtil;


    @DisplayName("로그인")
    @Test
    void signIn() throws Exception {
        // given
        SignInRequestDto signInRequestDto = new SignInRequestDto("test1234@naver.com", "test1234@");
        SignInResponseDto signInResponseDto = new SignInResponseDto();

        Member member = Member.builder()
                .id(1L)
                .email("test1234@gmail.com")
                .nickName("test")
                .build();

        String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTM2MDEwNjEsIm1lbWJlcklkIjoidGVzdDEyMzRAbmF2ZXIuY29tIn0.DZJzTFV45o-sm4CaHy7F09_B0llBEosAQdw9suGF1xo";
        String refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTM2MDQwNjcsIm1lbWJlcklkIjoidGVzdDEyMzRAbmF2ZXIuY29tIiwidXVpZCI6ImNjZTEyYWQxLTVjYWEtNDYyOC05ZTFmLTVmZWI0NzE4NGJkMSJ9.wpMnZr5DxywBEMWXGVhyzyVTt1U_taU1msKSjJlOhMc";

        ResponseCookie accessCookie = ResponseCookie
                .from(Constants.ACCESS_TOKEN, accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None") //개발 완료후 제거
                .maxAge(-1)
                .build();

        ResponseCookie refreshCookie = ResponseCookie
                .from(Constants.REFRESH_TOKEN, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None") //개발 완료후 제거
                .maxAge(-1)
                .build();

        // when
        when(memberService.signIn(any())).thenReturn(member);
        when(jwtUtil.createAccessToken(any())).thenReturn("Dummy data");
        when(jwtUtil.createRefreshToken(any(),any())).thenReturn("Dummy data");
        when(cookieUtil.createToken(eq(Constants.ACCESS_TOKEN), any())).thenReturn(accessCookie);
        when(cookieUtil.createToken(eq(Constants.REFRESH_TOKEN), any())).thenReturn(refreshCookie);

        ResultActions perform = mockMvc.perform(post("/member/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequestDto)));

                // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(notNullValue()))
                .andExpect(cookie().exists(Constants.ACCESS_TOKEN))
                .andExpect(cookie().exists(Constants.REFRESH_TOKEN))

                // spring rest docs
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("멤버 이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")),
                                responseFields(
                                        beneathPath("response").withSubsectionId("response"),
//                                        fieldWithPath("response.code").type(JsonFieldType.STRING).description("비밀번호"),
//                                        fieldWithPath("response.message").type(JsonFieldType.STRING).description("비밀번호"),
//                                        fieldWithPath("code").description("응답 코드"),
//                                        fieldWithPath("message").description("응답 메시지"),
                                        fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임"))
                        )
                );
    }

    @DisplayName("회원가입")
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
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("sex").type(JsonFieldType.STRING).description("성별"),
                                        fieldWithPath("birthDate").type(JsonFieldType.STRING).description("생년월일")
                                )
                        ));

    }

    @DisplayName("로그아웃")
    @Test
    void signOut() throws Exception {
        // given
        ResponseCookie acCookie = ResponseCookie
                .from(Constants.ACCESS_TOKEN)
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie rfCookie = ResponseCookie
                .from(Constants.REFRESH_TOKEN)
                .path("/")
                .maxAge(0)
                .build();

        // when
        when(authUtil.getUUID(any())).thenReturn(UUID.randomUUID().toString());
        when(authUtil.checkAuthReturnId(any())).thenReturn(2L);
        when(cookieUtil.deleteToken(Constants.ACCESS_TOKEN)).thenReturn(acCookie);
        when(cookieUtil.deleteToken(Constants.REFRESH_TOKEN)).thenReturn(rfCookie);

        mockMvc.perform(post("/member/sign-out")
                        .contentType(MediaType.APPLICATION_JSON))

        // then
                .andExpect(status().isOk())

                // spring rest docs
                .andDo(
                        restDocs.document());

    }

    @DisplayName("이메일 인증 메일 발송")
    @Test
    void sendMail() throws Exception {
        // given
        EmailAuthenticationDto dto = new EmailAuthenticationDto("newTestTest@naver.com");

        // when
        mockMvc.perform(post("/member/send-mail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))

                // then
                .andExpect(status().isOk())

                // spring rest docs
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                                )
                        ));

    }
}