package com.side.freedomdaybackend.domain;

import com.side.freedomdaybackend.common.interceptor.JwtInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureRestDocs // SpringRestDocs 자동 설정
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest
@Import(RestDocsConfiguration.class)
public abstract class RestDocsTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    RestDocumentationResultHandler restDocs;

    @MockBean
    JwtInterceptor jwtInterceptor;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .uris() // (4)
                        .withScheme("http")
                        .withHost("localhost")
                        .withPort(8080))
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print()) // 항상 프린트
                .alwaysDo(restDocs)
                .build();

        when(jwtInterceptor.preHandle(any(), any(), any())).thenReturn(true);

    }

}
