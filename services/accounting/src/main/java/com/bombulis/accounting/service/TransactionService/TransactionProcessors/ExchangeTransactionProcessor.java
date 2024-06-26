package com.bombulis.accounting.service.TransactionService.TransactionProcessors;

import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.dto.TransactionDTO;
import com.bombulis.accounting.entity.CurrencyAccount;
import com.bombulis.accounting.entity.TransactionAccount;
import com.bombulis.accounting.entity.TransactionCurrencyExchange;
import com.bombulis.accounting.entity.User;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.AccountService;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ExchangeTransactionProcessor implements TransactionProcessor {

    private AccountService accountService;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public TransactionAccount processCreateTransaction(TransactionDTO transactionDTO, User user) throws AccountNonFound, AccountTypeMismatchException {
        CurrencyAccount senderAccount = accountService.findAccount(transactionDTO.getSenderAccountId(), user.getUserId());
        CurrencyAccount receiverAccount = accountService.findAccount(transactionDTO.getReceivedAccountId(), user.getUserId());

        if (senderAccount.equals(receiverAccount)) {
            throw new AccountNonFound("Accounts are equal");
        }
        if (transactionDTO.getExchangeRate() == null) {
            throw new AccountNonFound("Exchange Rate is null");
        }
        if (transactionDTO.getReceivedAmount() == null) {
            throw new AccountNonFound("Received Amount is null");
        }

        TransactionCurrencyExchange transactionAccount = new TransactionCurrencyExchange(
                transactionDTO.getDescription(),
                transactionDTO.getSendAmount(),
                transactionDTO.getReceivedAmount(),
                transactionDTO.getExchangeRate(),
                senderAccount,
                receiverAccount,
                transactionDTO.getTransactionDate(),
                user,
                TransactionType.EXCHANGE.name());

        senderAccount.setBalance(senderAccount.getBalance().subtract(transactionDTO.getSendAmount()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(transactionDTO.getReceivedAmount()));
        return transactionAccount;
    }

    @Override
    public TransactionType getType() {
        return TransactionType.EXCHANGE;
    }

    @Override
    public BalanceDTO processCalculateBalance(TransactionAccount transaction, BalanceDTO balanceDTO) {
        final long accountId = balanceDTO.getAccountId();
        if (transaction.getRecipientAccount().getId().equals(accountId)) {
            balanceDTO.setBalance(balanceDTO.getBalance().add(((TransactionCurrencyExchange) transaction).getReceivedAmount()));
        } else if (transaction.getSenderAccount().getId().equals(accountId)) {
            balanceDTO.setBalance(balanceDTO.getBalance().subtract(((TransactionCurrencyExchange) transaction).getSendAmount()));
        }
        return balanceDTO;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
