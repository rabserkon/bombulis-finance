package com.bombulis.accounting.service.TransactionService.BalanceProcessors;

import com.bombulis.accounting.dto.Acc_BalanceDTO;
import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.entity.Acc_TransactionAccount;
import com.bombulis.accounting.repository.Acc_TransactionRepository;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.Acc_AccountType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.Acc_CurrencyMismatchException;
import com.bombulis.accounting.service.TransactionService.TransactionProcessors.Acc_TransactionProcessor;
import com.bombulis.accounting.service.TransactionService.TransactionProcessors.Acc_TransactionProcessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class Acc_CurrencyAccountBalanceProcessor implements Acc_BalanceProcessor {

    private Acc_TransactionRepository transactionRepository;
    private Acc_TransactionProcessorFactory transactionProcessorFactory;

    @Override
    public Acc_BalanceDTO process(Acc_Account account) throws Acc_AccountNonFound, Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException {
        final Long accountId = account.getId();
        Acc_BalanceDTO balanceDTO = new Acc_BalanceDTO(accountId, BigDecimal.ZERO);
        List<? extends Acc_TransactionAccount> transactions = transactionRepository.findTransactionAccountsBySenderAccountIdOrRecipientAccountId(accountId, accountId);
        for (Acc_TransactionAccount transaction : transactions) {
            Acc_TransactionProcessor transactionProcessor = transactionProcessorFactory.getProcessor(transaction.getType());
            balanceDTO = transactionProcessor.processCalculateBalance(transaction, balanceDTO);
        }
        return balanceDTO;
    }

    @Override
    public Acc_AccountType getType() {
        return Acc_AccountType.CURRENCY;
    }

    @Autowired
    public void setTransactionProcessorFactory(Acc_TransactionProcessorFactory transactionProcessorFactory) {
        this.transactionProcessorFactory = transactionProcessorFactory;
    }

    @Autowired
    public void setTransactionRepository(Acc_TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}
