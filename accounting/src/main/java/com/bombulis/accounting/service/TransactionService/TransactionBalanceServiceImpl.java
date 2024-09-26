package com.bombulis.accounting.service.TransactionService;

import com.bombulis.accounting.entity.*;
import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.dto.TransactionDTO;
import com.bombulis.accounting.repository.AccountRepository;
import com.bombulis.accounting.repository.TransactionRepository;
import com.bombulis.accounting.repository.UserRepository;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.AccountOtherType;
import com.bombulis.accounting.service.AccountService.AccountService;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.BalanceProcessors.BalanceProcessor;
import com.bombulis.accounting.service.TransactionService.BalanceProcessors.BalanceProcessorFactory;
import com.bombulis.accounting.service.TransactionService.TransactionProcessors.TransactionProcessor;
import com.bombulis.accounting.service.TransactionService.TransactionProcessors.TransactionProcessorFactory;
import com.bombulis.accounting.service.TransactionService.exception.CurrencyMismatchException;
import com.bombulis.accounting.service.UserService.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransactionBalanceServiceImpl implements TransactionBalanceService {

    private AccountService accountService;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private TransactionProcessorFactory transactionProcessorFactory;
    private BalanceProcessorFactory balanceProcessorFactory;


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TransactionAccount createTransaction(TransactionDTO transactionDTO, Long userId) throws AccountNonFound, CurrencyMismatchException, AccountTypeMismatchException, UserException {
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(()->new UserException("User not found"));
        TransactionProcessor processor = transactionProcessorFactory.getProcessor(transactionDTO.getType());
        TransactionAccount transactionAccount = processor.processCreateTransaction(transactionDTO, user);
        if (transactionAccount == null) {
            throw new AccountNonFound("Transaction is bad");
        }
        accountRepository.save(transactionAccount.getSenderAccount());
        accountRepository.save(transactionAccount.getRecipientAccount());
        return transactionRepository.save(transactionAccount);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BalanceDTO balanceReconciliation(Long accountId, Long userId) throws AccountNonFound, AccountTypeMismatchException, AccountOtherType, CurrencyMismatchException {
        CurrencyAccount currencyAccount = accountService.findAccount(accountId,userId);
        if (!currencyAccount.getUser().getUserId().equals(userId)){
            throw new AccountOtherType("Account not found");
        }
        if (currencyAccount.getClass().equals(CurrencyAccount.class)) {
            throw new AccountOtherType("Account type is bad: " + currencyAccount.getType());
        }
        BalanceDTO balance = calculateBalance(currencyAccount.getId(), userId);
        if (currencyAccount.getBalance().equals(balance.getBalance())){
            return balance;
        } else {
            currencyAccount.setBalance(balance.getBalance());

        }
        return balance;
    }

    @Override
    @Transactional(readOnly = true)
    public BalanceDTO calculateBalance(Long accountId, Long userId) throws AccountNonFound, CurrencyMismatchException, AccountTypeMismatchException {
        Account account = accountRepository.findAccountByIdAndUserUserId(accountId, userId)
                .orElseThrow(() -> new AccountNonFound("This account not found"));
        BalanceProcessor balanceProcessor = balanceProcessorFactory.getProcessor(account.getType());
        return balanceProcessor.process(account);
    }

    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public CurrencyAccount calculateBalance(CurrencyAccount account, Long userId) throws CurrencyMismatchException, AccountTypeMismatchException, AccountNonFound {
        account.setBalance(calculateBalance(account.getId(), userId).getBalance());
        return account;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setTransactionProcessorFactory(TransactionProcessorFactory transactionProcessorFactory) {
        this.transactionProcessorFactory = transactionProcessorFactory;
    }

    @Autowired
    public void setBalanceProcessorFactory(BalanceProcessorFactory balanceProcessorFactory) {
        this.balanceProcessorFactory = balanceProcessorFactory;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
