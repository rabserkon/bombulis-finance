package com.bombulis.accounting.service.TransactionService.TransactionProcessors;

import com.bombulis.accounting.dto.Acc_BalanceDTO;
import com.bombulis.accounting.dto.Acc_TransactionDTO;
import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.entity.Acc_CurrencyAccount;
import com.bombulis.accounting.entity.Acc_TransactionAccount;
import com.bombulis.accounting.entity.Acc_User;
import com.bombulis.accounting.repository.Acc_AccountRepository;
import com.bombulis.accounting.repository.Acc_TransactionRepository;
import com.bombulis.accounting.service.AccountService.Acc_AccountType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.Acc_AccountService;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.Acc_CurrencyMismatchException;
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
public class Acc_DefaultTransactionProcessor implements Acc_TransactionProcessor {

    private Acc_AccountService accountService;

    private Acc_AccountRepository accountRepository;

    private Acc_TransactionRepository transactionRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public Acc_TransactionAccount processCreateTransaction(Acc_TransactionDTO transactionDTO, Acc_User user) throws Acc_AccountNonFound, Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException {
        Acc_Account senderAccount = accountService.findAccount(transactionDTO.getSenderAccountId(), user.getUserId());
        Acc_Account receiverAccount = accountService.findAccount(transactionDTO.getReceivedAccountId(), user.getUserId());
        boolean isSenderBroker = senderAccount.getType().equals(Acc_AccountType.BROKERCURRENCY.name().toUpperCase());
        boolean isReceiverBroker = receiverAccount.getType().equals(Acc_AccountType.BROKERCURRENCY.name().toUpperCase());
        Acc_TransactionType transactionType = null;
       if (isSenderBroker && isReceiverBroker){
            transactionType = Acc_TransactionType.BROKERTRANSACTION;
        } else {
            transactionType = Acc_TransactionType.DEFAULT;
        }

        if (senderAccount.equals(receiverAccount)) {
            throw new Acc_AccountNonFound("Accounts are equal");
        }

        if (senderAccount instanceof Acc_CurrencyAccount && receiverAccount instanceof Acc_CurrencyAccount) {
            Acc_CurrencyAccount senderCurrencyAccount = (Acc_CurrencyAccount) senderAccount;
            Acc_CurrencyAccount receiverCurrencyAccount = (Acc_CurrencyAccount) receiverAccount;

            if (!senderCurrencyAccount.getCurrency().equals(receiverCurrencyAccount.getCurrency())) {
                throw new Acc_AccountNonFound("Currency not equal");
            }

            Acc_TransactionAccount transactionAccount = new Acc_TransactionAccount();
            transactionAccount.setDescription(transactionDTO.getDescription());
            transactionAccount.setSendAmount(transactionDTO.getSendAmount());
            transactionAccount.setTransactionDate(new Timestamp(transactionDTO.getTransactionDate().getTime()));
            transactionAccount.setUser(user);
            transactionAccount.setType(transactionType.name());
            transactionAccount.setSenderAccount(senderAccount);
            transactionAccount.setRecipientAccount(receiverAccount);

            senderCurrencyAccount.setBalance(senderCurrencyAccount.getBalance().subtract(transactionDTO.getSendAmount()));
            receiverCurrencyAccount.setBalance(receiverCurrencyAccount.getBalance().add(transactionDTO.getSendAmount()));
            accountRepository.save(senderCurrencyAccount);
            accountRepository.save(receiverCurrencyAccount);
            return transactionRepository.save(transactionAccount);
        } else {
            throw new IllegalArgumentException("Один или оба аккаунта не являются CurrencyAccount");
        }
    }

    @Override
    public Acc_BalanceDTO processCalculateBalance(Acc_TransactionAccount transaction, Acc_BalanceDTO balanceDTO) {
        final long accountId = balanceDTO.getAccountId();
        if (transaction.getRecipientAccount().getId().equals(accountId)) {
            balanceDTO.setBalance(balanceDTO.getBalance().add(transaction.getSendAmount()));
        } else if (transaction.getSenderAccount().getId().equals(accountId)) {
            balanceDTO.setBalance(balanceDTO.getBalance().subtract(transaction.getSendAmount()));
        }

        return balanceDTO;
    }

    @Override
    public Acc_TransactionType getType() {
        return Acc_TransactionType.DEFAULT;
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
