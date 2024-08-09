package com.bombulis.accounting.service.ReportService;

import com.bombulis.accounting.config.DatabaseConfig;
import com.bombulis.accounting.config.ModuleConfig;
import com.bombulis.accounting.dao.TransactionDaoIntegrationTest;
import com.bombulis.accounting.model.dao.AccountReport;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ModuleConfig.class, DatabaseConfig.class})
@ActiveProfiles("dev")
@TestPropertySource(properties = {
        "spring.profiles.active=dev",
})
public class ReportServiceIntegrationTest {

    @Autowired
    private ReportService reportService;

    private static final Logger logger = LoggerFactory.getLogger(TransactionDaoIntegrationTest.class);


    @Test
    void  testCreateAccountReport() throws AccountTypeMismatchException, AccountNonFound, AccountOtherType {
        Long userId = 5L;
        Long accountId = 18L;
        LocalDateTime startPeriod = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endPeriod = LocalDateTime.of(2024, 12, 31, 23, 59);

        AccountReport accountReport = reportService.createAccountReport(accountId,userId, startPeriod, endPeriod);

        logger.info(accountReport.toString());



    }
}
