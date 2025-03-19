//package com.gin.wegd.auth_service.kafka.config;
//
//import io.confluent.kafka.serializers.KafkaAvroDeserializer;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableKafka
//public class KafkaConsumerConfig {
//
//    @Bean
//    public ConsumerFactory<String, Object> consumerFactory() {
//        Map<String, Object> config = new HashMap<>();
//        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
//        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        config.put("schema.registry.url", "http://localhost:8089");
//        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
//        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
//        return new DefaultKafkaConsumerFactory<>(config);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
//}