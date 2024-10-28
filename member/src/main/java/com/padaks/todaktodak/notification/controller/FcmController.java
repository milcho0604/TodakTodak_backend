package com.padaks.todaktodak.notification.controller;

import com.padaks.todaktodak.common.dto.CommonErrorDto;
import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.member.dto.FcmTokenSaveRequest;
import com.padaks.todaktodak.notification.domain.FcmNotification;
import com.padaks.todaktodak.notification.dto.NotificationResDto;
import com.padaks.todaktodak.notification.service.FcmService;
import com.padaks.todaktodak.notification.domain.Type;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/fcm")
public class FcmController {
    private final FcmService fcmService;
    private static final Logger logger = LoggerFactory.getLogger(FcmController.class);
//    @PostMapping("/token")
//    public ResponseEntity<?> saveFcmToken(
//            @RequestHeader("memberEmail") String memberEmail,
//            @RequestBody @Valid FcmTokenSaveRequest dto) {
//        try {
//            // 이메일과 FCM 토큰 정보를 사용하여 저장 로직 호출
//            fcmService.saveFcmToken(memberEmail, dto);
//
//            // 성공 응답 객체 생성
//            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "FCM 토큰이 성공적으로 저장되었습니다.", null);
//            return ResponseEntity.ok(commonResDto);
//
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            // 잘못된 요청으로 인한 예외 처리
//            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, "잘못된 요청: " + e.getMessage());
//            return ResponseEntity.badRequest().body(commonErrorDto);
//
//        } catch (EntityNotFoundException e) {
//            e.printStackTrace();
//            // 해당 이메일로 사용자를 찾을 수 없는 경우 예외 처리
//            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(commonErrorDto);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            // 기타 예외 처리
//            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commonErrorDto);
//        }
//    }

    @PostMapping("/token")
    public ResponseEntity<?> saveFcmToken(@RequestBody @Valid FcmTokenSaveRequest dto, Authentication authentication) {
        try {
            // 인증된 사용자 정보에서 이메일 추출
            String memberEmail = authentication.getName();  // authentication에서 이메일 추출

            // 이메일과 FCM 토큰 정보를 사용하여 저장 로직 호출
            fcmService.saveFcmToken(memberEmail, dto);

            // 성공 응답 객체 생성
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "FCM 토큰이 성공적으로 저장되었습니다.", null);
            return ResponseEntity.ok(commonResDto);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // 잘못된 요청으로 인한 예외 처리
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, "잘못된 요청: " + e.getMessage());
            return ResponseEntity.badRequest().body(commonErrorDto);

        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            // 해당 이메일로 사용자를 찾을 수 없는 경우 예외 처리
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(commonErrorDto);

        } catch (Exception e) {
            e.printStackTrace();
            // 기타 예외 처리
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commonErrorDto);
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
    public ResponseEntity<CommonResDto> myNotis(@PageableDefault(size = 10)Pageable pageable) {
        Page<NotificationResDto> notificationResList = fcmService.myNotis(pageable);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"알림 조회 성공",notificationResList),HttpStatus.OK);
    }

//    @GetMapping("/read/{id}")
//    public ResponseEntity<CommonResDto> read(@PathVariable Long id) {
//        FcmNotification fcmNotification = fcmService.read(id);
//        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"알림 읽음 처리 성공",fcmNotification),HttpStatus.OK);
//    }
    @GetMapping("/read/{id}")
    public ResponseEntity<CommonResDto> read(@PathVariable Long id) {
        logger.info("Received request to mark notification as read. Notification ID: {}", id);

        try {
            FcmNotification fcmNotification = fcmService.read(id);
            logger.info("Notification successfully marked as read: {}", fcmNotification);
            return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "알림 읽음 처리 성공", fcmNotification), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while marking notification as read. Notification ID: {}, Error: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(new CommonResDto(HttpStatus.INTERNAL_SERVER_ERROR, "알림 읽음 처리 실패", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> memberEmailMap) {
        try {
            String memberEmail = memberEmailMap.get("memberEmail");
            System.out.println("로그아웃 요청: " + memberEmail);
            fcmService.logout(memberEmail);
            System.out.println("로그아웃 성공");
            return ResponseEntity.ok("로그아웃이 성공적으로 처리되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("로그아웃에 실패했습니다: " + e.getMessage());
        }
    }
}
