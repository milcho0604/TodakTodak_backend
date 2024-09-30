package com.padaks.todaktodak.common.exception.exceptionType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
public enum ChatExceptionType implements ExceptionType{
    CHATROOM_NOT_FOUND(BAD_REQUEST, "채팅방이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus httpStatus(){
        return null;
    }

    @Override
    public String message(){
        return null;
    }
}
