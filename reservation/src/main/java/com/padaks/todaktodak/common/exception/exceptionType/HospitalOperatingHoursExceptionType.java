package com.padaks.todaktodak.common.exception.exceptionType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
public enum HospitalOperatingHoursExceptionType implements ExceptionType{

        HOSPITAL_OPERATING_HOURS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 병원 영업시간이 존재하지 않습니다."),
        ALREADY_DELETED_HOSPITAL_OPERATING_HOURS(HttpStatus.BAD_REQUEST, "이미 삭제된 병원 영업시간입니다."),
        MISMATCHED_HOSPITAL(HttpStatus.BAD_REQUEST, "해당 스케쥴이 등록된 병원과 일치하지 않습니다."),
        INVALID_HOSPITAL_IMAGE(HttpStatus.BAD_REQUEST, "잘못된 형식의 사진입니다."),
        INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청 데이터입니다."),
        UNAUTHORIZED_ACTION(HttpStatus.FORBIDDEN, "권한이 없습니다.");


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
