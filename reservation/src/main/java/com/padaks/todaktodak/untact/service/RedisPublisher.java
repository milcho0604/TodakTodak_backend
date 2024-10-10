package com.padaks.todaktodak.untact.service;

import com.padaks.todaktodak.untact.domain.WebSocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class RedisPublisher {
    @Qualifier("untactRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    @Autowired
    public RedisPublisher(@Qualifier("untactRedisTemplate") RedisTemplate<String, Object> redisTemplate, ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    public void publish(WebSocketMessage message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
