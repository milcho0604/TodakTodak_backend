package com.padaks.todaktodak.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.padaks.todaktodak.common.dto.DtoMapper;
import com.padaks.todaktodak.common.exception.BaseException;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.repository.MemberRepository;
import com.padaks.todaktodak.member.service.FcmService;
import com.padaks.todaktodak.notification.domain.Notification;
import com.padaks.todaktodak.notification.domain.Type;
import com.padaks.todaktodak.notification.dto.CommentSuccessDto;
import com.padaks.todaktodak.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaConsumer {
    private final RedisTemplate<String, Object> redisTemplate;
    private final MemberRepository memberRepository;
    private final FcmService fcmService;



    @KafkaListener(topics = "community-success", groupId = "group_id", containerFactory = "kafkaListenerContainerFactory")
    public void consumerNotification(String message){
        ObjectMapper objectMapper = new ObjectMapper();
        if (message.startsWith("\"") && message.endsWith("\"")) {
            message = message.substring(1, message.length() -1).replace("\"", "\"");
            message = message.replace("\\", "");
        }

        try {
            CommentSuccessDto dto = objectMapper.readValue(message, CommentSuccessDto.class);
            Member member = memberRepository.findByMemberEmail(dto.getReceiverEmail()).orElseThrow(()->new EntityNotFoundException("존재하지 않는 회원입니다."));

            String category = "";
            if (dto.getType().equals(Type.POST)){
                category = "질문 알림";
                fcmService.sendMessage(member.getId(), category, dto.getTitle()+"에 대한 답변이 작성되었습니다.",  Type.POST);

            }else if (dto.getType().equals(Type.COMMENT)){
                category = "답변 알림";
                String comment = "에 작성한 답변";
                fcmService.sendMessage(member.getId(), category, dto.getTitle()+comment+"에 대한 답변이 작성되었습니다.",  Type.COMMENT);

            }


        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
