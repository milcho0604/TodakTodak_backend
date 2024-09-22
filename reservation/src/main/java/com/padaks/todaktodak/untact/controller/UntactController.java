package com.padaks.todaktodak.untact.controller;


import com.padaks.todaktodak.untact.domain.Room;
import com.padaks.todaktodak.untact.dto.RoomRequest;
import com.padaks.todaktodak.untact.service.UntactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class UntactController {
    private final UntactService untactService;

    @Autowired
    public UntactController(final UntactService untactService) {
        this.untactService = untactService;
    }

    @GetMapping("/rooms")
    public List<Room> getRooms() {
        log.info("[getRooms] 실행");
        return untactService.getRooms();
    }

    @PostMapping("/room")
    public ResponseEntity<Void> createRoom(@RequestBody RoomRequest roomRequest) {
        log.info("[createRoom] 실행");
        untactService.processRoomSelection(roomRequest.getId(), roomRequest.getUuid());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/room/random")
    public ResponseEntity<Long> getRandomRoomNumber() {
        log.info("[getRandomRoomNumber] 실행");
        return ResponseEntity.ok(untactService.getRandomRoomNumber());
    }
}
