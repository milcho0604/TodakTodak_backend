package com.padaks.todaktodak.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.padaks.todaktodak.untact.socket.SignalHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    public String host;

    @Value("6379")
    public int port;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
//        timestamps 형식을 따르지 않도록
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        localdatetime 매핑을 위한 모듈 활성화
        mapper.registerModules(new JavaTimeModule(), new Jdk8Module());
        return mapper;
    }

    @Bean
    @Qualifier("1")
    public RedisConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(3);
        return new LettuceConnectionFactory(configuration);
    }
    @Bean
    @Qualifier("1")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("1") RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()));
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    @Qualifier("2")
    public RedisConnectionFactory redisScheduledConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(4);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    @Qualifier("2")
    public RedisTemplate<String, Object> redisScheduledTemplate(@Qualifier("2") RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()));
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
    @Bean(name = "untactConnectionFactory")
    public RedisConnectionFactory untactConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(6);
        return new LettuceConnectionFactory(configuration);
    }
    @Bean(name = "untactRedisTemplate")
    public RedisTemplate<String, Object> untactRedisTemplate(@Qualifier("untactConnectionFactory") RedisConnectionFactory untactConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(untactConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic("chatroom");
    }
    @Bean
    public RedisMessageListenerContainer redisContainer(@Qualifier("untactConnectionFactory") RedisConnectionFactory untactConnectionFactory,
                                                        MessageListenerAdapter listenerAdapter,
                                                        ChannelTopic topic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(untactConnectionFactory);
        container.addMessageListener(listenerAdapter, topic);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(SignalHandler redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber);
    }

    @Bean
    @Qualifier("roomConnectionFactory")
    public RedisConnectionFactory roomConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(11);
        return new LettuceConnectionFactory(configuration);
    }
    @Bean
    @Qualifier("roomRedisTemplate")
    public RedisTemplate<String, Object> roomRedisTemplate(@Qualifier("roomConnectionFactory") RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()));
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
