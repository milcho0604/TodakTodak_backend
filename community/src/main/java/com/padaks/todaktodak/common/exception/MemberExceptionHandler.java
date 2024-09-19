package com.padaks.todaktodak.common.exception;

import com.padaks.todaktodak.common.exception.exceptionType.ExceptionType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        ExceptionType exceptionType = e.getExceptionType();

        return ResponseEntity.status(exceptionType.httpStatus())
                .body(ErrorResponse.builder()
                        .name(exceptionType.name())
                        .message(exceptionType.message())
                        .httpStatus(e.exceptionType.httpStatus())
                        .build());
    }
}