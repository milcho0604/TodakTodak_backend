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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
@Slf4j
public class EventController {

    private final EventService eventService;

    // 캘린더 이벤트 생성
    @PostMapping("/create")
    public ResponseEntity<Void> createEvent(@RequestBody EventCreateReqDto dto) {
        eventService.createEvent(dto);
        return ResponseEntity.ok().build();
    }

    // 캘린더 이벤트 업데이트
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateEvent(@PathVariable Long id, @RequestBody EventUpdateReqDto dto) {
        eventService.UpdateEvent(id, dto);
        return ResponseEntity.ok().build();
    }

    // 자신의 캘린더 이벤트 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<Page<EventListResDto>> eventList(Pageable pageable) {
        Page<EventListResDto> eventList = eventService.eventListResDtos(pageable);
        return ResponseEntity.ok(eventList);
    }

    // 특정 이벤트 디테일 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<EventDetailResDto> eventDetail(@PathVariable Long id) {
        EventDetailResDto eventDetail = eventService.eventDetail(id);
        return ResponseEntity.ok(eventDetail);
    }

    // 캘린더 이벤트 삭제 (soft delete)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deletedEvent(id);
        return ResponseEntity.ok().build();
    }
}
