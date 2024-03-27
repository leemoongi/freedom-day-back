package com.side.freedomdaybackend.common.exception;

import com.side.freedomdaybackend.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 exception
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse> handleServiceException(CustomException e) {
        ApiResponse response = new ApiResponse(e.getErrorCode());
        int status = e.getErrorCode().getHttpStatus();
        HttpStatus httpStatus = HttpStatus.valueOf(status);

        return new ResponseEntity<>(response,httpStatus);
    }

//    @ExceptionHandler(LoginException.class)
//    public ResponseEntity<ErrorApiResponse> handleLoginException(LoginException e) {
//
//        ErrorApiResponse response = new ErrorApiResponse(e.getErrorCode());
//        int status = e.getErrorCode().getHttpStatus();
//        HttpStatus httpStatus = HttpStatus.valueOf(status);
//        response.setMessage(e.getPwdFailCount().toString());
//
//        return new ResponseEntity<>(response,httpStatus);
//    }

}
