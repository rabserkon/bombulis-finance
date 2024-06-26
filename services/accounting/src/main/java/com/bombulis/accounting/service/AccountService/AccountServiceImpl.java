package com.bombulis.accounting.service.AccountService;

import com.bombulis.accounting.dto.AccountEditDTO;
import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.dto.AccountDTO;
import com.bombulis.accounting.entity.FinancingSource;
import com.bombulis.accounting.entity.User;
import com.bombulis.accounting.entity.WithdrawalDestination;
import com.bombulis.accounting.repository.AccountRepository;
import com.bombulis.accounting.service.AccountService.AccountProcessors.AccountProcessor;
import com.bombulis.accounting.service.AccountService.AccountProcessors.AccountProcessorsFactory;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.CurrencyService.CurrencyNonFound;
import com.bombulis.accounting.service.UserService.NotFoundUser;
import com.bombulis.accounting.service.UserService.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountServiceImpl implements AccountService{

    private AccountRepository accountRepository;
    private UserService userService;
    private AccountProcessorsFactory accountProcessorsFactory;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    @Transactional(readOnly = true)
    public <T extends Account> T findAccount(Long accountId, Long userId) throws AccountNonFound, AccountTypeMismatchException {
        Account account = accountRepository.findAccountByIdAndUserUserIdAndDeletedFalse(accountId, userId);
        if (account == null) {
            throw new AccountNonFound("Account not found for account ID: " + accountId );
        }
        return (T) account;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT)
    public <T extends Account> T createAccount(AccountDTO accountDTO, Long userId) throws CurrencyNonFound, NotFoundUser, AccountOtherType {
        final User user = userService.findUser(userId);
        AccountProcessor accountProcessor = accountProcessorsFactory.getProcessor(accountDTO.getType());
        T account = accountProcessor.processCreateAccount(accountDTO, user);
        return account;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public <T extends Account> T deleteAccount(Long accountId, Long userId) throws AccountNonFound {
        Account account = accountRepository.findAccountByIdAndUserUserIdAndDeletedFalse(accountId, userId);
        if (account == null) {
            throw new AccountNonFound("Account not found for account ID: " + accountId);
        }
        account.setDeleted(true);
        return (T) accountRepository.save(account);
    }

    @Override
    public <T extends Account> Collection<T> findUserAccounts(Long userId) throws AccountNonFound {
        List<? extends Account> accountsList = accountRepository.findByUserUserIdAndDeletedFalse(userId);
        accountsList = accountsList.stream()
                .filter(account -> !(account instanceof FinancingSource) && !(account instanceof WithdrawalDestination))
                .collect(Collectors.toList());
        return (Collection<T>) accountsList;
    }

    @Override
    public <T extends Account> T editAccount(AccountEditDTO accountDTO, Long userId) throws CurrencyNonFound {
        T account = (T) accountRepository.findAccountByIdAndUserUserId(accountDTO.getId(), userId);
        account.setDescription(accountDTO.getDescription());
        account.setName(accountDTO.getName());
        account.setArchive(accountDTO.isArchive());
        return (T) accountRepository.save(account);
    }

    @Override
    public <T extends Account> T setBalance(Long accountId, Long userId, BalanceDTO balanceDTO) throws AccountNonFound {
        return null;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAccountProcessorsFactory(AccountProcessorsFactory accountProcessorsFactory) {
        this.accountProcessorsFactory = accountProcessorsFactory;
    }
}
