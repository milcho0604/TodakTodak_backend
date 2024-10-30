package com.padaks.todaktodak.common.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class ChatKafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServer;
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
        return new KafkaAdmin(configs);
    }
    @Bean(name = "ChatTopic")
    public NewTopic paymentSuccess(){
        return new NewTopic("ChatTopic", 10 , (short) 1);
    }
    // numPartitions : 토픽이 가질 파티션 수, replicationFactor : 토픽의 복제 인자

    @Bean(name = "chatKafkaProducerFactory")
    public ProducerFactory<String, Object> chatKafkaProducerFactory(){
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put("request.timeout.ms", "30000");
        config.put("session.timeout.ms", "30000");
        config.put("connections.max.idle.ms", "60000");
        config.put("reconnect.backoff.ms", "1000");
        config.put("reconnect.backoff.max.ms", "10000");

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean(name = "chatKafkaTemplate")
    public KafkaTemplate<String, Object> chatKafkaTemplate(){

        return new KafkaTemplate<>(chatKafkaProducerFactory());
    }
}
