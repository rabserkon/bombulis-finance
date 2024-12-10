package com.bombulis.accounting.service.AccountService;

import com.bombulis.accounting.dto.Acc_AccountDTO;
import com.bombulis.accounting.dto.Acc_AccountEditDTO;
import com.bombulis.accounting.dto.Acc_BalanceDTO;
import com.bombulis.accounting.entity.Acc_AccountType;
import com.bombulis.accounting.entity.*;
import com.bombulis.accounting.repository.Acc_AccountRepository;
import com.bombulis.accounting.repository.Acc_AccountTypeRepository;
import com.bombulis.accounting.service.AccountService.AccountProcessors.Acc_AccountProcessor;
import com.bombulis.accounting.service.AccountService.AccountProcessors.Acc_AccountProcessorsFactory;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountException;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyNonFound;
import com.bombulis.accounting.service.UserService.Acc_UserException;
import com.bombulis.accounting.service.UserService.Acc_UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class Acc_AccountServiceImpl implements Acc_AccountService, Acc_AccountTypeService {

    private Acc_AccountRepository accountRepository;
    private Acc_UserService userService;
    private Acc_AccountProcessorsFactory accountProcessorsFactory;
    private Acc_AccountTypeRepository accountTypeRepository;
    private RedisTemplate<String, Object> redisTemplate;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    @Transactional(readOnly = true)
    /*@Cacheable(value = "accounts", key = "#accountId")*/
    public <T extends Acc_Account> T findAccount(Long accountId, Long userId) throws Acc_AccountNonFound, Acc_AccountTypeMismatchException {
        Acc_Account account = accountRepository.findAccountByIdAndUserUserIdAndDeletedFalse(accountId, userId)
                .orElseThrow(() -> new Acc_AccountNonFound("Account non found"));
        return (T) account;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT)
   /* @CachePut(value = "accounts", key = "#result.id")*/
    public Acc_Account createAccount(Acc_AccountDTO accountDTO, Long userId) throws Acc_CurrencyNonFound, Acc_UserException, Acc_AccountException {
        final Acc_User user = userService.findUserById(userId);
        Acc_AccountProcessor accountProcessor = accountProcessorsFactory.getProcessor(accountDTO.getType());
        Acc_Account account = accountProcessor.processCreateAccount(accountDTO, user);
        return account;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
   /* @CacheEvict(value = "accounts", key = "#accountId")*/
    public Acc_Account deleteAccount(Long accountId, Long userId) throws Acc_AccountException {
        Acc_Account account = accountRepository.findAccountByIdAndUserUserIdAndDeletedFalse(accountId, userId)
                .orElseThrow(() -> new Acc_AccountNonFound("Account not found"));
        account.setDeleted(true);
        return accountRepository.save(account);
    }

    @Override
    public Collection<Acc_Account> findUserAccounts(Long userId) throws Acc_AccountNonFound {
        List<? extends Acc_Account> accountsList = accountRepository.findByUserUserIdAndDeletedFalse(userId);
        accountsList = accountsList.stream()
                .filter(account -> !(account instanceof Acc_FinancingSource) && !(account instanceof Acc_WithdrawalDestination))
                .collect(Collectors.toList());
        for (Acc_Account account : accountsList) {
            redisTemplate.opsForValue().set("accounts::" + account.getId(), account);
        }
        return (Collection<Acc_Account>) accountsList;
    }

    @Override
   /* @CachePut(value = "accounts", key = "#result.id")*/
    public Acc_Account editAccount(Acc_AccountEditDTO accountDTO, Long userId) throws Acc_AccountNonFound {
        Acc_Account account =  accountRepository.findAccountByIdAndUserUserId(accountDTO.getId(), userId)
                    .orElseThrow(() -> new Acc_AccountNonFound("Account not found"));
        account.setDescription(accountDTO.getDescription());
        account.setName(accountDTO.getName());
        account.setArchive(accountDTO.isArchive());
        return  accountRepository.save(account);
    }

    @Override
    public Acc_Account setBalance(Long accountId, Long userId, Acc_BalanceDTO balanceDTO) throws Acc_AccountNonFound {
        return null;
    }

    @Override
    public List<Acc_AccountType> getAllTypes() {
        return accountTypeRepository.findAccountTypeByArchivalFalse();
    }

    @Autowired
    public void setAccountRepository(Acc_AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setUserService(Acc_UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAccountProcessorsFactory(Acc_AccountProcessorsFactory accountProcessorsFactory) {
        this.accountProcessorsFactory = accountProcessorsFactory;
    }

    @Autowired
    public void setAccountTypeRepository(Acc_AccountTypeRepository accountTypeRepository) {
        this.accountTypeRepository = accountTypeRepository;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
