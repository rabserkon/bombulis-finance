package com.bombulis.accounting.service.ReportService;

import com.bombulis.accounting.config.Acc_KafkaProducerConfig;
import com.bombulis.accounting.dao.Acc_TransactionDao;
import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.entity.Acc_CurrencyAccount;
import com.bombulis.accounting.model.dao.Acc_AccountReportEvent;
import com.bombulis.accounting.model.dao.Acc_ConsolidateAccount;
import com.bombulis.accounting.model.dao.Acc_Transaction;
import com.bombulis.accounting.service.AccountService.Acc_AccountService;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import com.bombulis.accounting.service.KafkaProducerService.Acc_KafkaMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class Acc_ReportServiceImpl implements Acc_ReportService {

    private Acc_TransactionDao transactionDAO;
    private Acc_AccountService accountService;
    private Acc_KafkaMessageProducer kafkaMessageProducer;

    private static final String REPORT_TYPE_BANK_STATEMENT = "BANK_STATEMENT";

    @Override
    public Acc_AccountReportEvent createAccountReport(Long accountId,
                                                      Long userId,
                                                      LocalDateTime startPeriod,
                                                      LocalDateTime endPeriod) throws Acc_AccountTypeMismatchException, Acc_AccountNonFound, Acc_AccountOtherType {
        Acc_Account account = accountService.findAccount(accountId, userId);
        validateAccountType(account);
        Acc_AccountReportEvent accountReport = initializeAccountReport(account, startPeriod, endPeriod);
        List<Acc_Transaction> transactionList = getTransactions(accountId, startPeriod, endPeriod);
        accountReport.setTransactions(transactionList);
        Acc_ConsolidateAccount consolidateAccount = consolidateAccountOperations(accountId, startPeriod, endPeriod);
        accountReport.setConsolidateAccount(consolidateAccount);
        kafkaMessageProducer.sendAccountReportEvent(accountReport);
        return accountReport;
    }

    protected void validateAccountType(Acc_Account account) throws Acc_AccountOtherType {
        if (!(account instanceof Acc_CurrencyAccount)) {
            throw new Acc_AccountOtherType("Accounts of this type do not support statements");
        }
    }

    protected Acc_AccountReportEvent initializeAccountReport(Acc_Account account, LocalDateTime startPeriod, LocalDateTime endPeriod) {
        Acc_AccountReportEvent accountReport = new Acc_AccountReportEvent(REPORT_TYPE_BANK_STATEMENT);
        accountReport.setStatementPeriodStart(startPeriod);
        accountReport.setStatementPeriodEnd(endPeriod);
        accountReport.setAccountNumber(account.getId().toString());
        accountReport.setAccount(new com.bombulis.accounting.model.dao.Acc_Account((Acc_CurrencyAccount) account));
        return accountReport;
    }

    protected List<Acc_Transaction> getTransactions(Long accountId, LocalDateTime startPeriod, LocalDateTime endPeriod) {
        return transactionDAO.findTransactionByAccountOnPeriod(accountId, startPeriod, endPeriod, null);
    }

    protected Acc_ConsolidateAccount consolidateAccountOperations(Long accountId, LocalDateTime startPeriod, LocalDateTime endPeriod) {
        return transactionDAO.consolidateAccountOperationOnPeriod(accountId, startPeriod, endPeriod);
    }

    @Autowired
    public void setTransactionDAO(Acc_TransactionDao transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @Autowired
    public void setAccountService(Acc_AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setKafkaMessageProducer(Acc_KafkaMessageProducer kafkaMessageProducer) {
        this.kafkaMessageProducer = kafkaMessageProducer;
    }
}
