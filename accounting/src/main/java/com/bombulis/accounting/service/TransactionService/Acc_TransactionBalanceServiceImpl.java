package com.bombulis.accounting.service.TransactionService;

import com.bombulis.accounting.entity.*;
import com.bombulis.accounting.dto.Acc_BalanceDTO;
import com.bombulis.accounting.dto.Acc_TransactionDTO;
import com.bombulis.accounting.repository.Acc_AccountRepository;
import com.bombulis.accounting.repository.Acc_TransactionRepository;
import com.bombulis.accounting.repository.Acc_UserRepository;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountException;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountOtherType;
import com.bombulis.accounting.service.AccountService.Acc_AccountService;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.BalanceProcessors.Acc_BalanceProcessor;
import com.bombulis.accounting.service.TransactionService.BalanceProcessors.Acc_BalanceProcessorFactory;
import com.bombulis.accounting.service.TransactionService.TransactionProcessors.Acc_TransactionProcessor;
import com.bombulis.accounting.service.TransactionService.TransactionProcessors.Acc_TransactionProcessorFactory;
import com.bombulis.accounting.service.TransactionService.exception.Acc_CurrencyMismatchException;
import com.bombulis.accounting.service.UserService.Acc_UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class Acc_TransactionBalanceServiceImpl implements Acc_TransactionBalanceService {

    private Acc_AccountService accountService;
    private Acc_UserRepository userRepository;
    private Acc_TransactionRepository transactionRepository;
    private Acc_AccountRepository accountRepository;
    private Acc_TransactionProcessorFactory transactionProcessorFactory;
    private Acc_BalanceProcessorFactory balanceProcessorFactory;


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Acc_TransactionAccount createTransaction(Acc_TransactionDTO transactionDTO, Long userId) throws Acc_AccountException, Acc_UserException {
        Acc_User user = userRepository.findUserByUserId(userId)
                .orElseThrow(()->new Acc_UserException("User not found"));
        Acc_TransactionProcessor processor = transactionProcessorFactory.getProcessor(transactionDTO.getType());
        Acc_TransactionAccount transactionAccount = (Acc_TransactionAccount) processor.processCreateTransaction(transactionDTO, user);
        if (transactionAccount == null) {
            throw new Acc_AccountNonFound("Transaction is bad");
        }
        return transactionAccount;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Acc_BalanceDTO balanceReconciliation(Long accountId, Long userId) throws Acc_AccountNonFound, Acc_AccountTypeMismatchException, Acc_AccountOtherType, Acc_CurrencyMismatchException {
        Acc_CurrencyAccount currencyAccount = accountService.findAccount(accountId,userId);
        if (!currencyAccount.getUser().getUserId().equals(userId)){
            throw new Acc_AccountOtherType("Account not found");
        }
        if (currencyAccount.getClass().equals(Acc_CurrencyAccount.class)) {
            throw new Acc_AccountOtherType("Account type is bad: " + currencyAccount.getType());
        }
        Acc_BalanceDTO balance = calculateBalance(currencyAccount.getId(), userId);
        if (currencyAccount.getBalance().equals(balance.getBalance())){
            return balance;
        } else {
            currencyAccount.setBalance(balance.getBalance());

        }
        return balance;
    }

    @Override
    @Transactional(readOnly = true)
    public Acc_BalanceDTO calculateBalance(Long accountId, Long userId) throws Acc_AccountNonFound, Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException {
        Acc_Account account = accountRepository.findAccountByIdAndUserUserId(accountId, userId)
                .orElseThrow(() -> new Acc_AccountNonFound("This account not found"));
        Acc_BalanceProcessor balanceProcessor = balanceProcessorFactory.getProcessor(account.getType());
        return balanceProcessor.process(account);
    }

    @Autowired
    public void setTransactionRepository(Acc_TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Acc_CurrencyAccount calculateBalance(Acc_CurrencyAccount account, Long userId) throws Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException, Acc_AccountNonFound {
        account.setBalance(calculateBalance(account.getId(), userId).getBalance());
        return account;
    }

    @Autowired
    public void setUserRepository(Acc_UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setAccountRepository(Acc_AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setTransactionProcessorFactory(Acc_TransactionProcessorFactory transactionProcessorFactory) {
        this.transactionProcessorFactory = transactionProcessorFactory;
    }

    @Autowired
    public void setBalanceProcessorFactory(Acc_BalanceProcessorFactory balanceProcessorFactory) {
        this.balanceProcessorFactory = balanceProcessorFactory;
    }

    @Autowired
    public void setAccountService(Acc_AccountService accountService) {
        this.accountService = accountService;
    }
}
