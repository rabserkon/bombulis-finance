package com.bombulis.financereport.service.KafkaConsumerService;

import com.bombulis.financereport.dto.Rps_AccountReport;
import com.bombulis.financereport.service.ExcelReportService.Rps_ExcelReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Rps_MessageConsumer {

    @Autowired
    private Rps_ExcelReportService excelReportService;
    private static final Logger logger = LoggerFactory.getLogger(Rps_MessageConsumer.class);


    /*@KafkaListener(topics = "${kafka.topic.account-reports}", groupId = "${spring.kafka.consumer.group-id}")*/
    @KafkaListener(topics = "reports-topic", groupId = "reports-group")
    public void listen(String message) {
        try {
            if (message.contains("documentType")) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                Rps_AccountReport report = objectMapper.readValue(message, Rps_AccountReport.class);
                processAccountReport(report);
            } else {
                processOtherMessage(message);
            }
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }

    private void processAccountReport(Rps_AccountReport report) {
        logger.info(report.toString());
    }

    private void processOtherMessage(String message) {

    }
}
