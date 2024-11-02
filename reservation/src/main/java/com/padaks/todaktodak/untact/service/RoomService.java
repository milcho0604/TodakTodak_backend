package com.padaks.todaktodak.untact.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.padaks.todaktodak.untact.domain.Room;
import com.padaks.todaktodak.untact.domain.WebSocketMessage;
import com.padaks.todaktodak.untact.util.Parser;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Service
public class RoomService {
    private static final Logger log = LoggerFactory.getLogger(RoomService.class);
    private final Parser parser;
    // repository substitution since this is a very simple realization
    private final Set<Room> rooms = new TreeSet<>(Comparator.comparing(Room::getId));
    @Qualifier("roomRedisTemplate")
    private final RedisTemplate<String, Object> roomRedisTemplate;
    @Autowired
    private RedisPublisher redisPublisher;
    private final ObjectMapper objectMapper;

    private static final String ROOM_KEY_PREFIX = "room:";

    @Autowired
    public RoomService(final Parser parser, @Qualifier("roomRedisTemplate") RedisTemplate<String, Object> roomRedisTemplate, ObjectMapper objectMapper) {
        this.parser = parser;
        this.roomRedisTemplate = roomRedisTemplate;
        this.objectMapper = objectMapper;
    }

    // 서버 시작 시 Redis에서 방 목록을 불러와 rooms Set을 초기화
    @PostConstruct
    public void loadRoomsFromRedis() {
        log.info("방 정보 초기화");
        Set<String> roomKeys = roomRedisTemplate.keys(ROOM_KEY_PREFIX + "*");
        if (roomKeys != null) {
            for (String key : roomKeys) {
                Object roomData = roomRedisTemplate.opsForValue().get(key);
                if (roomData != null) {
                    try {
                        // roomData를 JsonNode로 변환하여 id 필드 추출
                        JsonNode roomNode = objectMapper.readTree(roomData.toString());
                        String id = roomNode.get("id").asText();
                        log.info(id);
                        // 추출한 id로 Room 객체 생성하여 RoomService에 추가
                        rooms.add(new Room(id));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
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
        saveRoomToRedis(room);
        return rooms.add(room);
    }

    // 방을 제거하면서 Redis에서도 삭제
    public void removeRoom(String roomId) {
        rooms.removeIf(room -> room.getId().equals(roomId));
        WebSocketMessage message = new WebSocketMessage();
        message.setType("ROOM_DELETED");
        message.setData(roomId);
        redisPublisher.publish(message);
        roomRedisTemplate.delete(ROOM_KEY_PREFIX + roomId);
    }
    // 방 ID를 이용하여 Room 객체 찾기
    public Optional<Room> findRoomByStringId(final String sid) {
        // simple get() because of parser errors handling
        return rooms.stream().filter(r -> r.getId().equals(parser.parseId(sid).get())).findAny();
    }

    // Room 객체를 이용하여 방 ID 반환
    public String getRoomId(Room room) {
        return room.getId();
    }

    // 방에 속한 참여자 정보 반환
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

    // Redis에 방 정보 저장
    private void saveRoomToRedis(Room room) {
        try {
            String roomData = objectMapper.writeValueAsString(room);
            roomRedisTemplate.opsForValue().set(ROOM_KEY_PREFIX + room.getId(), roomData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
