package com.bombulis.accounting.service.TransactionService.TransactionProcessors;

import com.bombulis.accounting.dto.Acc_BalanceDTO;
import com.bombulis.accounting.dto.Acc_TransactionDTO;
import com.bombulis.accounting.entity.Acc_CurrencyAccount;
import com.bombulis.accounting.entity.Acc_TransactionAccount;
import com.bombulis.accounting.entity.Acc_TransactionCurrencyExchange;
import com.bombulis.accounting.entity.Acc_User;
import com.bombulis.accounting.repository.Acc_AccountRepository;
import com.bombulis.accounting.repository.Acc_TransactionRepository;
import com.bombulis.accounting.service.AccountService.Acc_AccountType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.Acc_AccountService;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;

@Service
@Transactional
public class Acc_ExchangeTransactionProcessor implements Acc_TransactionProcessor {

    private Acc_AccountService accountService;
    private Acc_AccountRepository accountRepository;
    private Acc_TransactionRepository transactionRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public Acc_TransactionAccount processCreateTransaction(Acc_TransactionDTO transactionDTO, Acc_User user) throws Acc_AccountNonFound, Acc_AccountTypeMismatchException {
        Acc_CurrencyAccount senderAccount = accountService.findAccount(transactionDTO.getSenderAccountId(), user.getUserId());
        Acc_CurrencyAccount receiverAccount = accountService.findAccount(transactionDTO.getReceivedAccountId(), user.getUserId());
        boolean isSenderBroker = senderAccount.getType().equals(Acc_AccountType.BROKERCURRENCY.name().toUpperCase());
        boolean isReceiverBroker = receiverAccount.getType().equals(Acc_AccountType.BROKERCURRENCY.name().toUpperCase());
        Acc_TransactionType transactionType = null;
        if (isSenderBroker != isReceiverBroker) {
            throw new Acc_AccountNonFound("It is not possible to conduct a transaction between a brokerage and a non-brokerage account");
        } else if (isSenderBroker == isReceiverBroker){
            transactionType = Acc_TransactionType.BROKEREXCHANGE;
        } else {
            transactionType = Acc_TransactionType.EXCHANGE;
        }
        if (senderAccount.equals(receiverAccount)) {
            throw new Acc_AccountNonFound("Accounts are equal");
        }
        if (transactionDTO.getExchangeRate() == null) {
            throw new Acc_AccountNonFound("Exchange Rate is null");
        }
        if (transactionDTO.getReceivedAmount() == null) {
            throw new Acc_AccountNonFound("Received Amount is null");
        }

        Acc_TransactionCurrencyExchange transactionAccount = new Acc_TransactionCurrencyExchange();

        transactionAccount.setDescription(transactionDTO.getDescription());
        transactionAccount.setSendAmount(transactionDTO.getSendAmount());
        transactionAccount.setReceivedAmount(transactionDTO.getReceivedAmount());
        transactionAccount.setExchangeRate(transactionDTO.getExchangeRate());
        transactionAccount.setTransactionDate(new Timestamp(transactionDTO.getTransactionDate().getTime()));
        transactionAccount.setUser(user);
        transactionAccount.setType(Acc_TransactionType.EXCHANGE.name());
        transactionAccount.setSenderAccount(senderAccount);
        transactionAccount.setRecipientAccount(receiverAccount);

        senderAccount.setBalance(senderAccount.getBalance().subtract(transactionDTO.getSendAmount()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(transactionDTO.getReceivedAmount()));
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);
        return transactionRepository.save(transactionAccount);
    }

    @Override
    public Acc_TransactionType getType() {
        return Acc_TransactionType.EXCHANGE;
    }

    @Override
    public Acc_BalanceDTO processCalculateBalance(Acc_TransactionAccount transaction, Acc_BalanceDTO balanceDTO) {
        final long accountId = balanceDTO.getAccountId();
        if (transaction.getRecipientAccount().getId().equals(accountId)) {
            balanceDTO.setBalance(balanceDTO.getBalance().add(((Acc_TransactionCurrencyExchange) transaction).getReceivedAmount()));
        } else if (transaction.getSenderAccount().getId().equals(accountId)) {
            balanceDTO.setBalance(balanceDTO.getBalance().subtract(((Acc_TransactionCurrencyExchange) transaction).getSendAmount()));
        }
        return balanceDTO;
    }

    @Autowired
    public void setAccountRepository(Acc_AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setTransactionRepository(Acc_TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Autowired
    public void setAccountService(Acc_AccountService accountService) {
        this.accountService = accountService;
    }
}
