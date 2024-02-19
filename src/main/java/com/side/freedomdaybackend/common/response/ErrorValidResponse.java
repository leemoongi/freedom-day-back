package com.side.freedomdaybackend.common.response;

import com.side.freedomdaybackend.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ErrorValidResponse extends ErrorResponse{

    private List<ValidError> validErrors;

    public ErrorValidResponse(ErrorCode errorCode, List<FieldError> fieldErrors) {
        super(errorCode);
        this.validErrors = fieldErrors.stream()
                .map(error -> new ValidError(
                        error.getField(),
                        error.getDefaultMessage()))
                .collect(Collectors.toList());
    }


    @Getter
    @AllArgsConstructor
    public static class ValidError {
        private String field;
        private String validMessage;
    }

}
