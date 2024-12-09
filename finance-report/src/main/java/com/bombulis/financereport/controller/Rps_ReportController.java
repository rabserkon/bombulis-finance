package com.bombulis.financereport.controller;

import com.bombulis.financereport.dto.Rps_Account;
import com.bombulis.financereport.dto.Rps_AccountReport;
import com.bombulis.financereport.dto.Rps_ConsolidateAccount;
import com.bombulis.financereport.dto.Rps_Transaction;
import com.bombulis.financereport.service.ExcelReportService.Rps_ExcelReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@RequestMapping("/api/reports")
public class Rps_ReportController {

    @Autowired
    private Rps_ExcelReportService excelReportService;

    ///api/reports/download/test
    @GetMapping("/download/test")
    public void downloadReport(HttpServletResponse response) throws IOException {
        // Example data
        Rps_Account account = new Rps_Account();
        account.setAccountId(123L);
        account.setName("John Doe");
        account.setCurrency("USD");
        account.setDescription("Main Account");
        account.setType("Checking");
        account.setSubType("Personal");

        Rps_ConsolidateAccount consolidateAccount = new Rps_ConsolidateAccount();
        consolidateAccount.setAccountId(123L);
        consolidateAccount.setTotalReceiveAmount(BigDecimal.valueOf(1000));
        consolidateAccount.setTotalSendAmount(BigDecimal.valueOf(500));
        consolidateAccount.setBalanceBeforeStartPeriod(BigDecimal.valueOf(1000));
        consolidateAccount.setBalanceAfterEndPeriod(BigDecimal.valueOf(1500));

        Rps_Transaction transaction = new Rps_Transaction();
        transaction.setId(1L);
        transaction.setSenderAccountId(123L);
        transaction.setReceivedAccountId(456L);
        transaction.setDescription("Payment");
        transaction.setType("Credit");
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setSendAmount(BigDecimal.valueOf(100));
        transaction.setReceivedAmount(BigDecimal.valueOf(100));
        transaction.setExchangeRate(BigDecimal.ONE);

        Rps_AccountReport report = new Rps_AccountReport();
        report.setStatementId("STAT123");
        report.setAccountNumber("1234567890");
        report.setAccountHolderName("John Doe");
        report.setStatementPeriodStart((LocalDateTime.now().minusDays(30)));
        report.setStatementPeriodEnd((LocalDateTime.now()));
        report.setAccount(account);
        report.setConsolidateAccount(consolidateAccount);
        report.setTransactions(Arrays.asList(transaction));

        byte[] excelFile = excelReportService.generateExcelReport(report);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=report.xlsx");
        response.getOutputStream().write(excelFile);
        response.getOutputStream().flush();
    }
}
