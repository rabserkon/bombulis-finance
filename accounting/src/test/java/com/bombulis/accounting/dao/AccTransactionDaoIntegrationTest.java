package com.bombulis.accounting.dao;


import com.bombulis.accounting.config.Acc_DatabaseConfig;
import com.bombulis.accounting.config.Acc_ModuleConfig;
import com.bombulis.accounting.model.dao.Acc_Transaction;
import com.bombulis.accounting.service.TransactionService.TransactionProcessors.Acc_TransactionType;
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
import java.util.List;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Acc_ModuleConfig.class, Acc_DatabaseConfig.class})
@ActiveProfiles("dev")
@TestPropertySource(properties = {
        "spring.profiles.active=dev",
})
public class AccTransactionDaoIntegrationTest {
    @Autowired
    private Acc_TransactionDao transactionDao;

    private static final Logger logger = LoggerFactory.getLogger(AccTransactionDaoIntegrationTest.class);


    @Test
    void testFindTransactionByAccountOnPeriod() {
        Long accountId = 18L;
        LocalDateTime startPeriod = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endPeriod = LocalDateTime.of(2024, 12, 31, 23, 59);
        Acc_TransactionType transactionType = null;

        List<Acc_Transaction> transactions = transactionDao.findTransactionByAccountOnPeriod(accountId, startPeriod, endPeriod, transactionType);

        transactions.forEach(System.out::println);


     /*   assertEquals(1, transactions.size());
        Transaction transaction = transactions.get(0);
        assertEquals("Test Transaction", transaction.getDescription());
        assertEquals(BigDecimal.valueOf(100.00), transaction.getSendAmount());
        assertEquals(BigDecimal.valueOf(100.00), transaction.getReceivedAmount());
        assertEquals("Sender Name", transaction.getSenderName());
        assertEquals("Receiver Name", transaction.getReceiverName());*/
    }

    @Test
    void testConsolidateAccountOperationOnPeriod (){
        Long accountId = 18L;
        LocalDateTime startPeriod = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endPeriod = LocalDateTime.of(2024, 12, 31, 23, 59);

        /*List<ConsolidateAccount> consolidateAccount = transactionDao.consolidateAccountOperationOnPeriod(accountId, startPeriod, endPeriod);

        logger.info(consolidateAccount.toString());*/


    }


}
