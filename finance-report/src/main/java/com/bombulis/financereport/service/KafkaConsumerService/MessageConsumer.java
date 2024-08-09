package com.bombulis.financereport.service.KafkaConsumerService;

import com.bombulis.financereport.dto.AccountReport;
import com.bombulis.financereport.service.ExcelReportService.ExcelReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageConsumer {

    @Autowired
    private ExcelReportService excelReportService;
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);


    /*@KafkaListener(topics = "${kafka.topic.account-reports}", groupId = "${spring.kafka.consumer.group-id}")*/
    @KafkaListener(topics = "reports-topic", groupId = "reports-group")
    public void listen(String message) {
        try {
            if (message.contains("statementId")) {
                ObjectMapper objectMapper = new ObjectMapper();
                AccountReport report = objectMapper.readValue(message, AccountReport.class);
                processAccountReport(report);
            } else {
                processOtherMessage(message);
            }
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }

    private void processAccountReport(AccountReport report) {
        logger.info(report.toString());
    }

    private void processOtherMessage(String message) {

    }
}
