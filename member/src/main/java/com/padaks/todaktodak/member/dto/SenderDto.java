//package com.padaks.todaktodak.member.dto;
//
//import com.amazonaws.services.simpleemail.model.*;
//import lombok.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class SenderDto {
//    private String from = "토닥 <milcho0604@gmail.com>"; // 보내는사람이름 <이메일주소>
//    private List<String> to; // ArrayList<String>에서 List<String>으로 변경
//    private String subject;
//    private String content;
//
//    public SenderDto(List<String> to, String subject, String content) {
//        this.to = to;
//        this.subject = subject;
//        this.content = content;
//        System.out.println(to);
//    }
//
//
//    public void addTo(String email) {
//        if (this.to == null) {
//            this.to = new ArrayList<>();
//        }
//        this.to.add(email);
//    }
//
//    public SendEmailRequest toSendRequestDto() {
//        Destination destination = new Destination().withToAddresses(to);
//        Message message = new Message()
//                .withSubject(createContent(subject))
//                .withBody(new Body().withHtml(createContent(content)));
//        return new SendEmailRequest()
//                .withSource(from)
//                .withDestination(destination)
//                .withMessage(message);
//    }
//
//    private Content createContent(String text) {
//        return new Content().withCharset("UTF-8").withData(text);
//    }
//}
