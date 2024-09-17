package com.padaks.todaktodak.common.exception.exceptionType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
public enum HospitalExceptionType implements ExceptionType{

        HOSPITAL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 병원이 존재하지 않습니다."),
        ALREADY_DELETED_HOSPITAL(HttpStatus.BAD_REQUEST, "이미 삭제된 병원입니다."),
        INVALID_HOSPITAL_IMAGE(HttpStatus.BAD_REQUEST, "잘못된 형식의 사진입니다."),
        INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청 데이터입니다."),
        UNAUTHORIZED_ACTION(HttpStatus.FORBIDDEN, "권한이 없습니다."),
        DOUBLE_CREATE(HttpStatus.BAD_REQUEST, "해당 프로젝트의 완성 글이 존재합니다.");

        private final HttpStatus httpStatus;
        private final String message;

        @Override
        public HttpStatus httpStatus() {
            return httpStatus;
        }

        @Override
        public String message() {
            return message;
        }



}
