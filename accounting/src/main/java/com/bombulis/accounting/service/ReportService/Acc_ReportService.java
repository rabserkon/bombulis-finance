package com.bombulis.accounting.service.ReportService;

import com.bombulis.accounting.dao.Acc_TransactionDao;
import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.entity.Acc_CurrencyAccount;
import com.bombulis.accounting.model.dao.Acc_AccountReport;
import com.bombulis.accounting.model.dao.Acc_ConsolidateAccount;
import com.bombulis.accounting.model.dao.Acc_Transaction;
import com.bombulis.accounting.service.AccountService.Acc_AccountService;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class Acc_ReportService {

    @Autowired
    private Acc_TransactionDao transactionDAO;

    @Autowired
    private Acc_AccountService accountService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "reports-topic";



    protected Acc_AccountReport createAccountReport(Long accountId,
                                                    Long userId,
                                                    LocalDateTime startPeriod,
                                                    LocalDateTime endPeriod) throws Acc_AccountTypeMismatchException, Acc_AccountNonFound, Acc_AccountOtherType {
        Acc_Account account = accountService.findAccount(accountId, userId);
        if (!(account instanceof Acc_CurrencyAccount)){
            throw new Acc_AccountOtherType("Аккаунты данного типа не поддерживают выписку");
        }
        Acc_AccountReport accountReport = new Acc_AccountReport("BANK_STATEMENT");
        accountReport.setStatementPeriodStart(startPeriod);
        accountReport.setStatementPeriodEnd(endPeriod);
        accountReport.setAccountNumber(account.getId().toString());
        accountReport.setAccount(new com.bombulis.accounting.model.dao.Acc_Account((Acc_CurrencyAccount) account));

        List<Acc_Transaction> transactionList = transactionDAO.findTransactionByAccountOnPeriod(
                accountId, startPeriod, endPeriod, null
        );

        accountReport.setTransactions(transactionList);

        Acc_ConsolidateAccount consolidateAccount = transactionDAO.consolidateAccountOperationOnPeriod(
                accountId, startPeriod, endPeriod
        );

        accountReport.setConsolidateAccount(consolidateAccount);

        return accountReport;
    }

    public void sendAccountReport(Acc_AccountReport accountReport){
        kafkaTemplate.send(TOPIC, accountReport);
    }
}
