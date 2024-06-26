package com.bombulis.accounting.service.TransactionService.TransactionProcessors;

import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.dto.TransactionDTO;
import com.bombulis.accounting.entity.*;
import com.bombulis.accounting.repository.TransactionRepository;
import com.bombulis.accounting.repository.UserRepository;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.AccountService;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.CurrencyMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DepositTransactionProcessor implements TransactionProcessor{

    private AccountService accountService;
    private TransactionRepository transactionRepository;
    private UserRepository userRepository;

    @Override
    public TransactionAccount processCreateTransaction(TransactionDTO transactionDTO, User user) throws AccountNonFound, CurrencyMismatchException, AccountTypeMismatchException {
        FinancingSource depositAccount = accountService.findAccount(transactionDTO.getSenderAccountId(), user.getUserId());
        CurrencyAccount receivedCurrencyAccount = accountService.findAccount(transactionDTO.getReceivedAccountId(), user.getUserId());
        if (!depositAccount.getClass().equals(FinancingSource.class)) {
            throw new AccountTypeMismatchException("Account sender other type");
        }
        if (!receivedCurrencyAccount.getClass().equals(CurrencyAccount.class)) {
            throw new AccountTypeMismatchException("Account received other type");
        }
        if (!((FinancingSource) depositAccount ).getCurrency().equals(receivedCurrencyAccount.getCurrency())){
            throw new CurrencyMismatchException("Cannot transfer between accounts with different currencies.");
        }
        TransactionAccount transactionAccount = new TransactionAccount(transactionDTO.getDescription(),
                transactionDTO.getReceivedAmount(),
                depositAccount,
                receivedCurrencyAccount,
                transactionDTO.getTransactionDate(),
                user,
                TransactionType.DEPOSIT.name());

        depositAccount.setBalance(depositAccount.getBalance().add(transactionDTO.getSendAmount()));
        receivedCurrencyAccount.setBalance(receivedCurrencyAccount.getBalance().add(transactionDTO.getReceivedAmount()));

        return transactionAccount;
    }

    @Override
    public BalanceDTO processCalculateBalance(TransactionAccount transactionAccount, BalanceDTO balanceDTO) {
        return null;
    }

    @Override
    public TransactionType getType() {
        return TransactionType.DEPOSIT;
    }

    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
