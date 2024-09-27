package com.padaks.todaktodak.chatroom.service;

import com.padaks.todaktodak.chatroom.domain.UntactChatRoom;
import com.padaks.todaktodak.chatroom.repository.UntactChatRoomRepository;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.untact.service.UntactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UntactChatRoomService {
    private final UntactChatRoomRepository untactChatRoomRepository;
    private final UntactService untactService;


    public void untactCreate(Reservation reservation) {
        String roomCode = UUID.randomUUID().toString();
        untactChatRoomRepository.save(UntactChatRoom.builder()
                .reservation(reservation)
                .code(roomCode)
                .build());
        untactService.processRoomSelection(roomCode);
    }
}
