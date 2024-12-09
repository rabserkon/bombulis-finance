package com.bombulis.accounting.service.TransactionService.TransactionProcessors;

import com.bombulis.accounting.dto.Acc_BalanceDTO;
import com.bombulis.accounting.dto.Acc_TransactionDTO;
import com.bombulis.accounting.entity.*;
import com.bombulis.accounting.repository.Acc_AccountRepository;
import com.bombulis.accounting.repository.Acc_TransactionRepository;
import com.bombulis.accounting.repository.Acc_UserRepository;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.Acc_AccountService;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.Acc_CurrencyMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@Transactional
public class Acc_WithdrawalTransactionProcessor implements Acc_TransactionProcessor {

    private Acc_AccountService accountService;
    private Acc_TransactionRepository transactionRepository;
    private Acc_UserRepository userRepository;
    private Acc_AccountRepository accountRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Acc_TransactionAccount processCreateTransaction(Acc_TransactionDTO transactionDTO, Acc_User user) throws Acc_AccountNonFound, Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException {
        Acc_CurrencyAccount senderAccount = accountService.findAccount(transactionDTO.getSenderAccountId(), user.getUserId());
        Acc_WithdrawalDestination withdrawalAccount = accountService.findAccount(transactionDTO.getReceivedAccountId(), user.getUserId());
        if (!senderAccount.getClass().equals(Acc_CurrencyAccount.class)) {
            throw new Acc_AccountTypeMismatchException("Account sender other type");
        }
        if (!withdrawalAccount.getClass().equals(Acc_WithdrawalDestination.class)) {
            throw new Acc_AccountTypeMismatchException("Account received other type");
        }
        if (!((Acc_CurrencyAccount) senderAccount ).getCurrency().equals(withdrawalAccount.getCurrency())){
            throw new Acc_CurrencyMismatchException("Cannot transfer between accounts with different currencies.");
        }
        Acc_TransactionCurrencyExchange transactionAccount =  new Acc_TransactionCurrencyExchange();
        transactionAccount.setDescription(transactionDTO.getDescription());
        transactionAccount.setSendAmount(transactionDTO.getSendAmount());
        transactionAccount.setReceivedAmount((transactionDTO.getReceivedAmount() != null ? transactionDTO.getReceivedAmount() : transactionDTO.getSendAmount()));
        transactionAccount.setExchangeRate(transactionDTO.getExchangeRate() != null ? transactionDTO.getExchangeRate()  : transactionDTO.getSendAmount());
        transactionAccount.setTransactionDate(new Timestamp(transactionDTO.getTransactionDate().getTime()));
        transactionAccount.setUser(user);
        transactionAccount.setType(Acc_TransactionType.WITHDRAWAL.name());
        transactionAccount.setSenderAccount(senderAccount);
        transactionAccount.setRecipientAccount(withdrawalAccount);

        senderAccount.setBalance(senderAccount.getBalance().subtract(transactionDTO.getSendAmount()));
        withdrawalAccount.setBalance(withdrawalAccount.getBalance().add(transactionDTO.getReceivedAmount() != null ? transactionDTO.getReceivedAmount() : transactionDTO.getSendAmount()));
        accountRepository.save(senderAccount);
        accountRepository.save(withdrawalAccount);
        return transactionRepository.save(transactionAccount);
    }

    @Override
    public Acc_BalanceDTO processCalculateBalance(Acc_TransactionAccount transactionAccount, Acc_BalanceDTO balanceDTO) {
        return null;
    }

    @Override
    public Acc_TransactionType getType() {
        return Acc_TransactionType.WITHDRAWAL;
    }

    @Autowired
    public void setTransactionRepository(Acc_TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Autowired
    public void setAccountRepository(Acc_AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Autowired
    public void setUserRepository(Acc_UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setAccountService(Acc_AccountService accountService) {
        this.accountService = accountService;
    }
}
