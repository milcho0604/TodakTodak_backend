package com.padaks.todaktodak.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChildSuccessResDto {
    private String childName;
    private String sharer;
    private Long childId;
    private String memberEmail;

}
//: 자녀 공유 성공 메시지를 Kafka로 전송: {"childName":"rlackd","sharer":"정슬기","childId":2,"memberEmail":"tjqkdsla1217@naver.com"}