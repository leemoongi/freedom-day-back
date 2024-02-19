package com.side.freedomdaybackend.common.response;

import com.side.freedomdaybackend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ErrorResponse extends Response {

    public ErrorResponse(ErrorCode errorCode) {
        super.setSuccess(false);
        super.setCode(errorCode.getCode());
        super.setMessage(errorCode.getMessage());
    }
}
