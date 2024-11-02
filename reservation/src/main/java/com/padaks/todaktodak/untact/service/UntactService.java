package com.padaks.todaktodak.untact.service;

import com.padaks.todaktodak.untact.domain.Room;
import com.padaks.todaktodak.untact.domain.WebSocketMessage;
import com.padaks.todaktodak.untact.util.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UntactService {
    private final RoomService roomService;
    private final Parser parser;
    @Autowired
    private RedisPublisher redisPublisher;

    @Autowired
    public UntactService(final RoomService roomService, final Parser parser) {
        this.roomService = roomService;
        this.parser = parser;
    }

    public List<Room> getRooms() {
        return roomService.getRooms();
    }

    public void processRoomSelection(String sid) {
        Optional<String> optionalId = parser.parseId(sid);
        optionalId.ifPresent(id -> roomService.addRoom(new Room(id)));
        WebSocketMessage message = new WebSocketMessage();
        message.setType("ROOM_CREATED");
        message.setData(sid);
        redisPublisher.publish(message);
    }

    public Long getRandomRoomNumber() {
        return ThreadLocalRandom.current().nextLong(0, 100);
    }
}
