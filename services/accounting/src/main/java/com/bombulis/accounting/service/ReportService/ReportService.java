package com.bombulis.accounting.service.ReportService;

import com.bombulis.accounting.dao.TransactionDao;
import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.entity.CurrencyAccount;
import com.bombulis.accounting.model.dao.AccountReport;
import com.bombulis.accounting.model.dao.ConsolidateAccount;
import com.bombulis.accounting.model.dao.Transaction;
import com.bombulis.accounting.service.AccountService.AccountService;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private TransactionDao transactionDAO;

    @Autowired
    private AccountService accountService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "reports-topic";



    protected AccountReport createAccountReport( Long accountId,
                                                 Long userId,
                                                 LocalDateTime startPeriod,
                                                 LocalDateTime endPeriod) throws AccountTypeMismatchException, AccountNonFound, AccountOtherType {
        Account account = accountService.findAccount(accountId, userId);
        if (!(account instanceof CurrencyAccount)){
            throw new AccountOtherType("Аккаунты данного типа не поддерживают выписку");
        }
        AccountReport accountReport = new AccountReport();
        accountReport.setStatementPeriodStart(startPeriod);
        accountReport.setStatementPeriodEnd(endPeriod);
        accountReport.setAccountNumber(account.getId().toString());
        accountReport.setAccount(new com.bombulis.accounting.model.dao.Account((CurrencyAccount) account));

        List<Transaction> transactionList = transactionDAO.findTransactionByAccountOnPeriod(
                accountId, startPeriod, endPeriod, null
        );

        accountReport.setTransactions(transactionList);

        ConsolidateAccount consolidateAccount = transactionDAO.consolidateAccountOperationOnPeriod(
                accountId, startPeriod, endPeriod
        );

        accountReport.setConsolidateAccount(consolidateAccount);

        return accountReport;
    }

    public void sendAccountReport(AccountReport accountReport){
        kafkaTemplate.send(TOPIC, accountReport);
    }
}
