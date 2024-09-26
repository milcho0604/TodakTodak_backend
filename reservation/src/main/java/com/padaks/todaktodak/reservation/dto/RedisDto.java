package com.padaks.todaktodak.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisDto {
    private Long id;
    private String memberEmail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RedisDto redisDto = (RedisDto) o;
        return Objects.equals(id, redisDto.id) &&
                Objects.equals(memberEmail, redisDto.memberEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberEmail);
    }
}
