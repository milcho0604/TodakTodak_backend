package com.padaks.todaktodak.notification.controller;

import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.notification.dto.NotificationReqDto;
import com.padaks.todaktodak.notification.dto.NotificationResDto;
import com.padaks.todaktodak.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/create")
    public ResponseEntity<CommonResDto> notificationCreate(@RequestBody NotificationReqDto dto) {
        NotificationResDto notificationResDto = notificationService.notificationCreate(dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED,"알림 생성 성공",notificationResDto),HttpStatus.CREATED);
    }
    @GetMapping("/mynotis")
    public ResponseEntity<CommonResDto> mynotis(Pageable pageable) {
        Page<NotificationResDto> notificationResList = notificationService.mynotis(pageable);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"알림 조회 성공",notificationResList),HttpStatus.OK);
    }
    @GetMapping("/read/{id}")
    public ResponseEntity<CommonResDto> read(@PathVariable Long id) {
        NotificationResDto notificationResDto = notificationService.read(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"알림 읽음 처리 성공",notificationResDto),HttpStatus.OK);
    }

}
