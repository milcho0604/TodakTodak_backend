package com.padaks.todaktodak.event.service;

import com.padaks.todaktodak.event.domain.Event;
import com.padaks.todaktodak.event.dto.EventCreateReqDto;
import com.padaks.todaktodak.event.dto.EventDetailResDto;
import com.padaks.todaktodak.event.dto.EventListResDto;
import com.padaks.todaktodak.event.dto.EventUpdateReqDto;
import com.padaks.todaktodak.event.repository.EventRepository;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.domain.Role;
import com.padaks.todaktodak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;

    // 캘린더 이벤트 생성
    public void createEvent(EventCreateReqDto dto){
        Member member = memberRepository.findByMemberEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        eventRepository.save(dto.toEntity(member));
    }

    // 캘린더 이벤트 업데이트
    public void UpdateEvent(Long id, EventUpdateReqDto dto){
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이벤트입니다."));
        if (event.getMember().getMemberEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            event.toUpdate(dto);
            eventRepository.save(event);
        }
    }

    // 자신의 캘린더 리스트
    public Page<EventListResDto> eventListResDtos(Pageable pageable){
        Page<Event> events = eventRepository.findByMemberEmailAndDeletedAtIsNull(SecurityContextHolder.getContext().getAuthentication().getName(), pageable);
        return events.map(a -> a.listFromEntity());
    }

    // 이벤트 디테일
    public EventDetailResDto eventDetail(Long id){
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이벤트입니다."));
        return event.detailFromEntity();
    }

    // 이벤트 삭제
    public void deletedEvent(Long id){
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이벤트입니다."));
        event.setDeletedTimeAt(LocalDateTime.now());
        eventRepository.save(event);
    }
}
