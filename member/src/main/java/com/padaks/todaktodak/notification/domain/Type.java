package com.padaks.todaktodak.notification.domain;

public enum Type {
//    회원 가입 시 토닥 admin에게 가는 알림
    REGISTER,
//    예약 발생 시 병원 admin에게 가는 알림
    RESERVATION_NOTIFICATION,
//    대기 순번이 다가왔을 경우 예약자에게 가는 알림
    RESERVATION_WAITING,
//    게시글에 댓글이 달렸을 경우 게시글 작성자에게 가는 알림
    POST,
//    댓글에 대한 답글이 달렸을 경우 기존 댓글 작성자에게 가는 알림
    COMMENT,
//    결제 알림
    PAYMENT,
//    채팅 알림
    CHAT,
//    자녀 알림
    CHILD
}
