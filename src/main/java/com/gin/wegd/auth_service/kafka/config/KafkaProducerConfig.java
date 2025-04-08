package com.gin.wegd.auth_service.kafka.config;

import com.gin.wegd.auth_service.kafka.topics.AuthTopic;
import com.gin.wegd.common.events.OtpEvModel;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;


import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {


//    @Bean
//    public ProducerFactory<String, Object> producerFactory() {
//        Map<String, Object> config = new HashMap<>();
//        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
//        config.put(ProducerConfig.RETRIES_CONFIG, 3);
//        config.put(ProducerConfig.ACKS_CONFIG, "all");
//        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class );
//        config.put("schema.registry.url", "http://localhost:8089");
//        return new DefaultKafkaProducerFactory<>(config);
//    }
//    @Bean
//    public KafkaTemplate<String, Object> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }

    @Bean
    NewTopic forgetPasswordTopic() {
        return new NewTopic(AuthTopic.PASSWORD_RESET.getName(), 1, (short) 1);
    }

    @Bean
    NewTopic deleteAccountTopic() {
        return new NewTopic(AuthTopic.DELETE.getName(), 1, (short) 1);
    }

    @Bean
    NewTopic registerTopic() {
        return new NewTopic(AuthTopic.REGISTER.getName(), 1, (short) 1);
    }

    @Bean
    NewTopic sendOtpTopic() {
        return new NewTopic(AuthTopic.OTP.getName(), 1, (short) 1);
    }

    @Bean
    NewTopic baseNotifyEmailTopic() {
        return new NewTopic(AuthTopic.NOTIFY_EMAIL.getName(), 1, (short) 1);
    }


}
