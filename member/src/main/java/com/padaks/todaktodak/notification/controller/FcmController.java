package com.padaks.todaktodak.notification.controller;

import com.padaks.todaktodak.common.dto.CommonErrorDto;
import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.member.dto.FcmTokenSaveRequest;
import com.padaks.todaktodak.notification.dto.NotificationResDto;
import com.padaks.todaktodak.notification.service.FcmService;
import com.padaks.todaktodak.notification.domain.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/fcm")
public class FcmController {
    private final FcmService fcmService;

    @PostMapping("/token")
    public ResponseEntity<?> saveFcmToken(@RequestHeader String memberEmail, @RequestBody @Valid FcmTokenSaveRequest dto){
        try {
            fcmService.saveFcmToken(memberEmail, dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "token이 저장되었습니다.", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }catch (EntityNotFoundException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody String memberEmail, @RequestBody String title, String body, Type type, Long id){
        try {
            fcmService.sendMessage(memberEmail, title, body, type, id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "fcm 메세지 전송 성공", type);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "알림 전송 중 오류 발생: "+e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<CommonResDto> myNotis(Pageable pageable) {
        Page<NotificationResDto> notificationResList = fcmService.myNotis(pageable);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"알림 조회 성공",notificationResList),HttpStatus.OK);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<CommonResDto> read(@PathVariable Long id) {
        NotificationResDto notificationResDto = fcmService.read(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"알림 읽음 처리 성공",notificationResDto),HttpStatus.OK);
    }
}
