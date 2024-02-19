package com.side.freedomdaybackend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginException extends RuntimeException{

    private ErrorCode errorCode;

    private Integer pwdFailCount;

}
