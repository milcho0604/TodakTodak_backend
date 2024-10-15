package com.padaks.todaktodak.event.controller;

import com.padaks.todaktodak.event.dto.EventCreateReqDto;
import com.padaks.todaktodak.event.dto.EventDetailResDto;
import com.padaks.todaktodak.event.dto.EventListResDto;
import com.padaks.todaktodak.event.dto.EventUpdateReqDto;
import com.padaks.todaktodak.event.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
@Slf4j
public class EventController {

    private final EventService eventService;

    // 캘린더 이벤트 생성
    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@RequestBody EventCreateReqDto dto) {
        eventService.createEvent(dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    // 캘린더 이벤트 업데이트
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody EventUpdateReqDto dto) {
        eventService.UpdateEvent(id, dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    // 자신의 캘린더 이벤트 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<Page<?>> eventList(Pageable pageable) {
        Page<EventListResDto> eventList = eventService.eventListResDtos(pageable);
        return ResponseEntity.ok(eventList);
    }

    // 특정 이벤트 디테일 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> eventDetail(@PathVariable Long id) {
        EventDetailResDto eventDetail = eventService.eventDetail(id);
        return ResponseEntity.ok(eventDetail);
    }

    // 캘린더 이벤트 삭제 (soft delete)
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        eventService.deletedEvent(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
