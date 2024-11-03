//package com.padaks.todaktodak.common.config;
//
//import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
//import com.padaks.todaktodak.member.dto.SenderDto;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MailUtil {
//    private final AwsSesConfig awsSesConfig;
//
//    // 생성자를 통해 의존성 주입
//    public MailUtil(AwsSesConfig awsSesConfig) {
//        this.awsSesConfig = awsSesConfig;
//    }
//
//    // 인스턴스 메서드로 변경
//    public void send(SenderDto senderDto) {
//        try {
//            AmazonSimpleEmailService client = awsSesConfig.amazonSimpleEmailService();
//            client.sendEmail(senderDto.toSendRequestDto());
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new IllegalArgumentException("이메일 전송 서비스가 원활하지 않습니다.");
//        }
//    }
//}
