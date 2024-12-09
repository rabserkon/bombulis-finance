package com.bombulis.accounting.service.ReportService;

import com.bombulis.accounting.config.Acc_DatabaseConfig;
import com.bombulis.accounting.config.Acc_ModuleConfig;
import com.bombulis.accounting.dao.AccTransactionDaoIntegrationTest;
import com.bombulis.accounting.model.dao.Acc_AccountReport;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
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
@ContextConfiguration(classes = {Acc_ModuleConfig.class, Acc_DatabaseConfig.class})
@ActiveProfiles("dev")
@TestPropertySource(properties = {
        "spring.profiles.active=dev",
})
public class ReportServiceIntegrationTest {

    @Autowired
    private Acc_ReportService reportService;

    private static final Logger logger = LoggerFactory.getLogger(AccTransactionDaoIntegrationTest.class);


    @Test
    void  testCreateAccountReport() throws Acc_AccountTypeMismatchException, Acc_AccountNonFound, Acc_AccountOtherType {
        Long userId = 5L;
        Long accountId = 18L;
        LocalDateTime startPeriod = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endPeriod = LocalDateTime.of(2024, 12, 31, 23, 59);

        Acc_AccountReport accountReport = reportService.createAccountReport(accountId,userId, startPeriod, endPeriod);

        logger.info(accountReport.toString());

        reportService.sendAccountReport(accountReport);
    }
}
