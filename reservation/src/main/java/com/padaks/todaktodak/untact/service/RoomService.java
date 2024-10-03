package com.padaks.todaktodak.untact.service;


import com.padaks.todaktodak.untact.domain.Room;
import com.padaks.todaktodak.untact.util.Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Slf4j
@Service
public class RoomService {    
    private final Parser parser;
    // repository substitution since this is a very simple realization
    private final Set<Room> rooms = new TreeSet<>(Comparator.comparing(Room::getId));

    @Autowired
    public RoomService(final Parser parser) {
        this.parser = parser;
    }

    public List<Room> getRooms() {
        List<Room> defensiveCopy = new ArrayList<>(rooms);
        defensiveCopy.sort(Comparator.comparing(Room::getId));
        return Collections.unmodifiableList(defensiveCopy);
    }
    // roomId로 Room을 찾아 반환하는 함수
    public Optional<Room> findRoomById(final String roomId) {
        return rooms.stream().filter(r -> r.getId().equals(roomId)).findAny();
    }

    public Boolean addRoom(final Room room) {
        return rooms.add(room);
    }

    public Optional<Room> findRoomByStringId(final String sid) {
        // simple get() because of parser errors handling
        return rooms.stream().filter(r -> r.getId().equals(parser.parseId(sid).get())).findAny();
    }

    public String getRoomId(Room room) {
        return room.getId();
    }

    public Map<String, WebSocketSession> getClients(final Room room) {
        return Optional.ofNullable(room)
                .map(r -> Collections.unmodifiableMap(r.getClients()))
                .orElse(Collections.emptyMap());
    }

    public WebSocketSession addClient(final Room room, final String name, final WebSocketSession session) {
        return room.getClients().put(name, session);
    }

    public WebSocketSession removeClientByName(final Room room, final String name) {
        return room.getClients().remove(name);
    }
}
