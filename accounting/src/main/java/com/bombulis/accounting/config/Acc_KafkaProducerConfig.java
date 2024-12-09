package com.bombulis.accounting.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class Acc_KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaHost;

    private final String DLQ_TOPIC = "msg-dlq";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ErrorHandler kafkaErrorHandler() {
        return (exception, data) -> {
            logger.error("Error processing message: ", exception);
        };
    }



    private void sendMessageToDlq(ConsumerRecord<String, String> record) {
        try {
            kafkaTemplate().send(DLQ_TOPIC, record.key(), record.value());
            logger.error("Message sent to DLQ: " + record.value());
        } catch (Exception e) {
            logger.error("Failed to send message to DLQ: ", e);
        }
    }
}