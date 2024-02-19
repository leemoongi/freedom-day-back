package com.side.freedomdaybackend.common.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponse<T> extends Response {

    private T response;

    public SuccessResponse(T response) {
        super.setSuccess(true);
        super.setCode("SUCCESS");
        super.setMessage("성공입니다.");
        this.response = response;
    }
}
