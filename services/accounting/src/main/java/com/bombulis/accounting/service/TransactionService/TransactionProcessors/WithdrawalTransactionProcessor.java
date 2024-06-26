package com.bombulis.accounting.service.TransactionService.TransactionProcessors;

import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.dto.TransactionDTO;
import com.bombulis.accounting.entity.CurrencyAccount;
import com.bombulis.accounting.entity.TransactionAccount;
import com.bombulis.accounting.entity.User;
import com.bombulis.accounting.entity.WithdrawalDestination;
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
public class WithdrawalTransactionProcessor implements TransactionProcessor{

    private AccountService accountService;
    private TransactionRepository transactionRepository;
    private UserRepository userRepository;

    @Override
    public TransactionAccount processCreateTransaction(TransactionDTO transactionDTO, User user) throws AccountNonFound, CurrencyMismatchException, AccountTypeMismatchException {
        CurrencyAccount senderAccount = accountService.findAccount(transactionDTO.getSenderAccountId(), user.getUserId());
        WithdrawalDestination withdrawalAccount = accountService.findAccount(transactionDTO.getReceivedAccountId(), user.getUserId());
        if (!senderAccount.getClass().equals(CurrencyAccount.class)) {
            throw new AccountTypeMismatchException("Account sender other type");
        }
        if (!withdrawalAccount.getClass().equals(WithdrawalDestination.class)) {
            throw new AccountTypeMismatchException("Account received other type");
        }
        if (!((CurrencyAccount) senderAccount ).getCurrency().equals(withdrawalAccount.getCurrency())){
            throw new CurrencyMismatchException("Cannot transfer between accounts with different currencies.");
        }
        TransactionAccount transactionAccount = new TransactionAccount(transactionDTO.getDescription(),
                transactionDTO.getSendAmount(),
                senderAccount,
                withdrawalAccount,
                transactionDTO.getTransactionDate(),
                user,
                TransactionType.WITHDRAWAL.name());

        senderAccount.setBalance(senderAccount.getBalance().subtract(transactionDTO.getSendAmount()));
        withdrawalAccount.setBalance(withdrawalAccount.getBalance().add(transactionDTO.getReceivedAmount()));
        return transactionRepository.save(transactionAccount);
    }

    @Override
    public BalanceDTO processCalculateBalance(TransactionAccount transactionAccount, BalanceDTO balanceDTO) {
        return null;
    }

    @Override
    public TransactionType getType() {
        return TransactionType.WITHDRAWAL;
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
