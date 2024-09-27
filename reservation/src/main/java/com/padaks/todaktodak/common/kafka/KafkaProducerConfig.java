package com.padaks.todaktodak.common.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class KafkaProducerConfig {

    @Bean(name = "paymentSuccessTopic")
    public NewTopic paymentSuccess(){
        return new NewTopic("payment-success", 10 , (short) 1);
    }

    @Bean(name = "paymentFailTopic")
    public NewTopic paymentFail(){
        return new NewTopic("payment-fail", 10 , (short) 1);
    }

    @Bean(name = "paymentCancelTopic")
    public NewTopic paymentCancel(){
        return new NewTopic("payment-cancel", 10 , (short) 1);
    }

    @Bean(name = "paymentCancelFailTopic")
    public NewTopic paymentCancelFail(){
        return new NewTopic("payment-cancel-fail", 10 , (short) 1);
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);  // Optional for more compact headers
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
