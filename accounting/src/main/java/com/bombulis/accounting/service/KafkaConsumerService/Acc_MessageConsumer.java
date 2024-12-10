package com.bombulis.accounting.service.KafkaConsumerService;

import com.bombulis.accounting.entity.Acc_User;
import com.bombulis.accounting.service.UserService.Acc_UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Acc_MessageConsumer {

    private static final String KAFKA_TOPIK_REGISTRATION = "reg-msg";
    private final String DLQ_TOPIC = "msg-dlq";

    private Acc_UserService userService;
    private ObjectMapper objectMapper;
    private KafkaTemplate<String, Object> kafkaTemplate;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = "my-topic", groupId = "accounting-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }

    @KafkaListener(topics = "reg-msg", groupId = "auth-group")
    public void regMsgListen(ConsumerRecord<String, String> record) {
        try {
            Acc_RegistrationEvent event = objectMapper.readValue(record.value(), Acc_RegistrationEvent.class);
            Acc_User user = userService.createUser(event.getUserId());
            logger.debug("Registration new user with id: " + user.getUserId());
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

    @Autowired
    public void setUserService(Acc_UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
