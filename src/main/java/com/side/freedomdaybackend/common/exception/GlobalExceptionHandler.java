package com.side.freedomdaybackend.common.exception;

import com.side.freedomdaybackend.common.response.ErrorResponse;
import com.side.freedomdaybackend.common.response.ErrorValidResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 exception
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(CustomException e) {
        ErrorResponse response = new ErrorResponse(e.getErrorCode());
        int status = e.getErrorCode().getHttpStatus();
        HttpStatus httpStatus = HttpStatus.valueOf(status);

        return new ResponseEntity<>(response,httpStatus);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ErrorResponse> handleLoginException(LoginException e) {

        ErrorResponse response = new ErrorResponse(e.getErrorCode());
        int status = e.getErrorCode().getHttpStatus();
        HttpStatus httpStatus = HttpStatus.valueOf(status);
        response.setMessage(e.getPwdFailCount().toString());

        return new ResponseEntity<>(response,httpStatus);
    }


//    // @Valid 통과하지 못하면
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorValidResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
//        ErrorValidResponse fieldResponse = new ErrorValidResponse(ErrorCode.BAD_REQUEST, fieldErrors);
//
//        log.error("An error occurred: {}, StackTrace: {}", e.getMessage(), e);
//        return new ResponseEntity<>(fieldResponse, HttpStatus.BAD_REQUEST);
//    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleServerException(Exception e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(response, httpStatus);
    }
}
