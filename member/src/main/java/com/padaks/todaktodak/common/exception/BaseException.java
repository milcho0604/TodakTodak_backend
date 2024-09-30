package com.padaks.todaktodak.common.exception;

import com.padaks.todaktodak.common.exception.exceptionType.ChatExceptionType;
import com.padaks.todaktodak.common.exception.exceptionType.ExceptionType;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
    ExceptionType exceptionType;

    @Override
    public String getMessage(){
        return exceptionType.message();
    }
}
