package com.bombulis.accounting.service.TransactionService.BalanceProcessors;

import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.entity.TransactionAccount;
import com.bombulis.accounting.repository.TransactionRepository;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.AccountType;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.CurrencyMismatchException;
import com.bombulis.accounting.service.TransactionService.TransactionProcessors.TransactionProcessor;
import com.bombulis.accounting.service.TransactionService.TransactionProcessors.TransactionProcessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CurrencyAccountBalanceProcessor implements BalanceProcessor{

    private TransactionRepository transactionRepository;
    private TransactionProcessorFactory transactionProcessorFactory;

    @Override
    public BalanceDTO process(Account account) throws AccountNonFound, CurrencyMismatchException, AccountTypeMismatchException {
        final Long accountId = account.getId();
        BalanceDTO balanceDTO = new BalanceDTO(accountId,BigDecimal.ZERO);
        List<? extends TransactionAccount> transactions = transactionRepository.findTransactionAccountsBySenderAccountIdOrRecipientAccountId(accountId, accountId);
        for (TransactionAccount transaction : transactions) {
            TransactionProcessor transactionProcessor = transactionProcessorFactory.getProcessor(transaction.getType());
            balanceDTO = transactionProcessor.processCalculateBalance(transaction, balanceDTO);
        }
        return balanceDTO;
    }

    @Override
    public AccountType getType() {
        return AccountType.CURRENCY;
    }

    @Autowired
    public void setTransactionProcessorFactory(TransactionProcessorFactory transactionProcessorFactory) {
        this.transactionProcessorFactory = transactionProcessorFactory;
    }

    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}
