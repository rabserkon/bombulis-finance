package com.bombulis.stock.control.service.OperationService;

import com.bombulis.stock.control.entity.*;
import com.bombulis.stock.control.model.St_CurrencyAccount;
import com.bombulis.stock.control.model.St_OperationDTO;
import com.bombulis.stock.control.model.St_OperationStatus;
import com.bombulis.stock.control.model.St_StockMatchProfile;
import com.bombulis.stock.control.repository.St_AccountTypeRepository;
import com.bombulis.stock.control.repository.St_OperationRepository;
import com.bombulis.stock.control.service.AccountService.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class St_OperationServiceImpl implements St_OperationService{

    @Autowired
    private St_OperationRepository operationRepository;
    @Autowired
    private St_StockInfoService stockInfoService;
    @Autowired
    private St_CurrencyService currencyService;
    @Autowired
    private St_BrokerAccountService brokerAccountService;
    @Autowired
    private St_AccountService accountService;
    @Autowired
    private St_AccountTypeRepository accountTypeRepository;
    @Autowired
    private St_TransactionIntegrationService transactionIntegrationService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public St_Operation createOperation(St_OperationDTO operationForm, Long userId) throws St_AccountException, NotFoundTikerCompany, TransactionExceptionAccountService {
        St_StockMatchProfile companyName = stockInfoService.getCompanyInfoFromAlternativeApi(operationForm.getTiker());
        if (companyName == null) {
            throw new IllegalArgumentException("Компания с тикером " + operationForm.getTiker() + " не найдена.");
        }
        St_Currency currencyResponse = getCurrencyForCompany(companyName.getCurrency());
        St_CurrencyAccount currencyAccount = getCurrencyAccount(operationForm.getCurrencyAccountId(), userId);
        this.validateCurrencyMatch(currencyAccount, companyName);
        this.checkAccountBalance(currencyAccount);
        St_Broker broker = getBrokerAccount(operationForm.getBrokerAccountId(), userId);
        St_AccountType accountType = this.getAccountType("EQUITY");
        St_Account account = this.getOrCreateEquityAccount(companyName.getIsin(), userId, accountType, broker, companyName, operationForm.getDate());

        St_Operation operation = new St_Operation();
        operation.setAmount(operationForm.getPrice().multiply(new BigDecimal(operationForm.getQuantity())));
        operation.setPrice(operationForm.getPrice());
        operation.setQuantity(operationForm.getQuantity());
        operation.setCurrency(companyName.getCurrency());
        operation.setType(operationForm.getType());
        operation.setMCStatus(St_OperationStatus.COMPLETED.getCode());
        operation.setStatus(St_OperationStatus.COMPLETED.name().toUpperCase());
        operation.setUserId(userId);
        operation.setBroker(broker);
        operation.setCurrencyCode(currencyResponse.getCurrencyCode());
        operation.setAccount(account);
        operation.setCurrencyAccountId(currencyAccount.getAccountId());
        operation.setDate(operationForm.getDate() != null ? operationForm.getDate() : LocalDateTime.now());
        transactionIntegrationService.sendTransactionToAccount(operation);
        operation = operationRepository.save(operation);
        account = accountService.updateAccountBalance(account,
                operation,
                userId);
        return operation;
    }

    // Метод для получения валюты
    protected St_Currency getCurrencyForCompany(String currencyCode) {
        St_Currency currencyResponse = currencyService.findCurrency(currencyCode);
        if (currencyResponse == null) {
            throw new IllegalArgumentException("Валюта с тикером " + currencyCode + " не найдена.");
        }
        return currencyResponse;
    }

    // Метод для получения счета пользователя
    protected St_CurrencyAccount getCurrencyAccount(Long currencyAccountId, Long userId) throws St_AccountException {
        return accountService.getCurrencyAccountById(currencyAccountId, userId);
    }

    // Метод для получения брокерского счета
    protected St_Broker getBrokerAccount(Long brokerAccountId, Long userId) throws St_AccountException {
        return brokerAccountService.getBrokerAccount(brokerAccountId, userId);
    }

    // Проверка баланса на счете
    protected void checkAccountBalance(St_CurrencyAccount currencyAccount) throws St_AccountBalanceException {
        if (currencyAccount.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new St_AccountBalanceException("Account balance cannot be negative for account ID: "
                    + currencyAccount.getAccountId() + ". Current balance: " + currencyAccount.getBalance());
        }
    }

    // Метод для получения типа аккаунта
    protected St_AccountType getAccountType(String type) throws St_AccountException {
        return accountTypeRepository
                .findSt_AccountTypesByTypeAndActiveTrue(type)
                .orElseThrow(() -> new St_AccountException("This type note exist or accessible"));
    }

    // Метод для создания или получения аккаунта акций
    protected St_Account getOrCreateEquityAccount(String isin, Long userId, St_AccountType accountType, St_Broker broker, St_StockMatchProfile companyName, LocalDateTime date) throws St_AccountNotFoundException {
        return (St_Account) accountService.getOrCreateEquityAccount(isin, userId, accountType, broker, companyName, date);
    }

    protected void validateCurrencyMatch(St_CurrencyAccount currencyAccount, St_StockMatchProfile companyName) throws St_AccountException {
        if (!currencyAccount.getIsoCode().equals(companyName.getCurrency())) {
            throw new St_AccountException(
                    "The currency of the account (ID: " + currencyAccount.getAccountId() + " Currency:" + currencyAccount.getIsoCode() +
                            ") does not match the currency of the purchased financial instrument. " +
                            "The account is using currency: " + currencyAccount.getIsoCode() + ", while the instrument is using currency: " + companyName.getCurrency());
        }
    }
}
