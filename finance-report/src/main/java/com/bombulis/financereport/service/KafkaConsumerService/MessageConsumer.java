package com.bombulis.financereport.service.KafkaConsumerService;

import com.bombulis.financereport.dto.AccountReport;
import com.bombulis.financereport.service.ExcelReportService.ExcelReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageConsumer {

    @Autowired
    private ExcelReportService excelReportService;

    @KafkaListener(topics = "reports-topic", groupId = "reports-group")
    public  void listen(AccountReport report) {
        try {
            byte[] excelFile = excelReportService.generateExcelReport(report);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
