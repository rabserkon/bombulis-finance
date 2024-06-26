package com.bombulis.accounting.service.TransactionService.TransactionProcessors;

import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.dto.TransactionDTO;
import com.bombulis.accounting.entity.CurrencyAccount;
import com.bombulis.accounting.entity.TransactionAccount;
import com.bombulis.accounting.entity.User;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.AccountService;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.CurrencyMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DefaultTransactionProcessor implements TransactionProcessor{

    private AccountService accountService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public TransactionAccount processCreateTransaction(TransactionDTO transactionDTO, User user) throws AccountNonFound, CurrencyMismatchException, AccountTypeMismatchException {
        CurrencyAccount senderAccount = accountService.findAccount(transactionDTO.getSenderAccountId(), user.getUserId());
        CurrencyAccount receiverAccount = accountService.findAccount(transactionDTO.getReceivedAccountId(), user.getUserId());

        if (senderAccount.equals(receiverAccount)) {
            throw new AccountNonFound("Accounts are equal");
        }

        TransactionAccount transactionAccount = new TransactionAccount(
                transactionDTO.getDescription(),
                transactionDTO.getSendAmount(),
                senderAccount,
                receiverAccount,
                transactionDTO.getTransactionDate(),
                user,
                TransactionType.DEFAULT.name());
        senderAccount.setBalance(senderAccount.getBalance().subtract(transactionDTO.getSendAmount()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(transactionDTO.getSendAmount()));

        return transactionAccount;
    }

    @Override
    public BalanceDTO processCalculateBalance(TransactionAccount transaction, BalanceDTO balanceDTO) {
        final long accountId = balanceDTO.getAccountId();
        if (transaction.getRecipientAccount().getId().equals(accountId)) {
            balanceDTO.setBalance(balanceDTO.getBalance().add(transaction.getSendAmount()));
        } else if (transaction.getSenderAccount().getId().equals(accountId)) {
            balanceDTO.setBalance(balanceDTO.getBalance().subtract(transaction.getSendAmount()));
        }

        return balanceDTO;
    }

    @Override
    public TransactionType getType() {
        return TransactionType.DEFAULT;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
