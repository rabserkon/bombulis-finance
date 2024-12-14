package com.minecraftservers.app.service.ConsumerService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

public class ConsumerService {

    private ObjectMapper objectMapper;
    private KafkaTemplate<String, Object> kafkaTemplate;

    private final String DLQ_TOPIC = "mine-app-server-msg-dlq";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = "server-st", groupId = "mine-serv-group")
    public void regMsgListen(ConsumerRecord<String, String> record) {
        try {
           logger.info("Message: " + record.value());
        } catch (Exception e) {
            logger.error("Error processing Kafka message: ", e);
            sendMessageToDlq(record);
        }
    }


    private void sendMessageToDlq(ConsumerRecord<String, String> record) {
        try {
            kafkaTemplate.send(DLQ_TOPIC, record.key(), record.value());
            logger.error("Message sent to DLQ: " + record.value());
        } catch (Exception e) {
            logger.error("Failed to send message to DLQ: ", e);
        }
    }
}
