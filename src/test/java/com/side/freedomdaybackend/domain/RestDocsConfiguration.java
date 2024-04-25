package com.side.freedomdaybackend.domain;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@TestConfiguration
public class RestDocsConfiguration {
    @Bean
    public RestDocumentationResultHandler restDocsMockMvcConfigurationCustomizer() {
        return MockMvcRestDocumentation.document("{class-name}/{method-name}",
                Preprocessors.preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()));
    }
}