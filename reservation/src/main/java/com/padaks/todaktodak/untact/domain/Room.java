package com.padaks.todaktodak.untact.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.socket.WebSocketSession;

//import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Room {
//    @NotNull
    private final String id;
    // sockets by user names
    @JsonIgnore
    private final Map<String, WebSocketSession> clients = new HashMap<>();

    public Room(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Map<String, WebSocketSession> getClients() {
        return clients;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Room room = (Room) o;
        return Objects.equals(getId(), room.getId()) &&
                Objects.equals(getClients(), room.getClients());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getClients());
    }
}

