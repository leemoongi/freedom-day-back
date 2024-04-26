package com.side.freedomdaybackend.common.response;

import com.side.freedomdaybackend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@NoArgsConstructor
public class ApiResponse<T> {

    private String code;

    private String message;

    private T response;

    public ApiResponse() {
        this.code = "SUCCESS";
        this.message = "api 통신 성공";
        this.response = null;
    }

    public ApiResponse(T response) {
        this.code = "SUCCESS";
        this.message = "api 통신 성공";
        this.response = response;
    }

    public ApiResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }


}
