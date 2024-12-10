package com.bombulis.accounting.service.KafkaProducerService;

import com.bombulis.accounting.model.dao.Acc_AccountReportEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
public class Acc_KafkaMessageProducer {

    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String KAFKA_TOPIC_REPORT = "reports-topic";

    public void sendAccountReportEvent(Acc_AccountReportEvent event) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(KAFKA_TOPIC_REPORT, event);
        future.addCallback(
                result -> {
                    // write your Successful send logic here
                },
                ex -> {
                    // write your customer error handling logic here
                });
    }

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
