package com.padaks.todaktodak.common.exception.exceptionType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
public enum GlobalExceptionType implements ExceptionType{
    JSON_PARSING_ERROR(BAD_REQUEST, "Json 메시지 파싱 에러");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus httpStatus(){
        return status;
    }

    @Override
    public String message(){
        return message;
    }
}
