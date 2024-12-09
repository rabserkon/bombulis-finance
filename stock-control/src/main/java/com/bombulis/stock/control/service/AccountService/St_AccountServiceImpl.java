package com.bombulis.stock.control.service.AccountService;

import com.bombulis.stock.control.entity.*;
import com.bombulis.stock.control.model.St_CurrencyAccount;
import com.bombulis.stock.control.model.St_OperationType;
import com.bombulis.stock.control.model.St_StockMatchProfile;
import com.bombulis.stock.control.proto.AccountOuterClass;
import com.bombulis.stock.control.proto.AccountServiceGrpc;
import com.bombulis.stock.control.proto.Currency;
import com.bombulis.stock.control.repository.St_AccountRepository;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class St_AccountServiceImpl implements St_AccountService{

    private final RestTemplate restTemplate;
    private final ManagedChannel channel;
    private final AccountServiceGrpc.AccountServiceBlockingStub stub;
    private St_AccountRepository accountRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public St_AccountServiceImpl(ManagedChannel channel, RestTemplate restTemplate) {
        this.channel = channel;
        this.restTemplate = restTemplate;
        this.stub = AccountServiceGrpc.newBlockingStub(channel);
    }


    @Override
    public St_Account getOrCreateEquityAccount(String isin,
                                               Long userId,
                                               St_AccountType accountType,
                                               St_Broker broker,
                                               St_StockMatchProfile companyName,
                                               LocalDateTime date) throws St_AccountNotFoundException {
        St_EquityAccount equityAccount = (St_EquityAccount) this.getAccountByIdAndIsin(broker.getBrokerAccountId(), isin, userId);
        if (equityAccount == null) {
            equityAccount = new St_EquityAccount();
            equityAccount.setAccountType(accountType);
            equityAccount.setBroker(broker);
            equityAccount.setIsin(isin);
            equityAccount.setCurrency(companyName.getCurrency());
            equityAccount.setQuantity(0);
            equityAccount.setPrice(new BigDecimal("0.00"));
            equityAccount.setTotalValue(new BigDecimal("0.00"));
            equityAccount.setCreationDate(date);
            equityAccount.setLastOperationDate(date);
            equityAccount.setCompanyName(companyName.getCompanyName());
            equityAccount.setUserId(userId);
            equityAccount.setProfit(BigDecimal.ZERO);
            equityAccount = accountRepository.save(equityAccount);
        }
        return equityAccount;
    }

    @Override
    public St_Account getAccountById(Long brokerID, Long userId) throws St_AccountNotFoundException {
        return null;
    }

    @Override
    public St_Account getAccountByIdAndIsin(Long brokerID, String isin, Long userId) throws St_AccountNotFoundException {
        St_Account account = accountRepository.findSt_AccountByBrokerBrokerAccountIdAndIsinAndUserId(brokerID,isin,userId).orElse(null);;
        return account;
    }

    @Override
    public St_Account createAccountByIdAndIsin(Long brokerID, String isin, Long userId) throws St_AccountNotFoundException {
        return null;
    }

    @Override
    @Transactional
    public St_Account updateAccountBalance(St_Account account,
                                           St_Operation operation,
                                           Long userId) throws St_AccountException {
        final int quantityBeforeOperation = account.getQuantity();
        final BigDecimal financialBeforeResult = account.getProfit();
        final BigDecimal priceBeforeOperation = account.getPrice();
        if (operation.getType().equals(St_OperationType.BUY.name().toUpperCase())){
            account.setQuantity(account.getQuantity() + operation.getQuantity());
            account.setTotalValue(account.getTotalValue().add(operation.getAmount()));

            if (account.getQuantity() > 0) {
                if (quantityBeforeOperation < 0){
                    BigDecimal shortCloseProfit =  priceBeforeOperation.subtract(operation.getPrice())
                            .multiply(new BigDecimal(Math.abs(quantityBeforeOperation)));
                    account.setProfit(financialBeforeResult.add(shortCloseProfit));
                }
            } else {
                int closingQuantity = quantityBeforeOperation - operation.getQuantity().intValue();
                BigDecimal averagePrice = priceBeforeOperation;
                BigDecimal buyPrice = operation.getPrice();
                BigDecimal closeProfit =  averagePrice.subtract(buyPrice).multiply(new BigDecimal(closingQuantity));
                account.setProfit(financialBeforeResult.add(closeProfit));
            }
            if (account.getQuantity() != 0){
                BigDecimal price = account.getTotalValue()
                        .divide(new BigDecimal(account.getQuantity().intValue()),4 , BigDecimal.ROUND_HALF_UP);
                account.setPrice(price);
            } else {
                account.setTotalValue(new BigDecimal("0.0000"));
                account.setPrice(new BigDecimal("0.0000"));
            }
        } else if (operation.getType().equals(St_OperationType.SELL.name().toUpperCase())){
            account.setQuantity(account.getQuantity() - operation.getQuantity());
            account.setTotalValue(account.getTotalValue().subtract(operation.getAmount()));

            if (account.getQuantity() < 0) {
                if (quantityBeforeOperation > 0){
                    BigDecimal shortCloseProfit = operation.getPrice().subtract(priceBeforeOperation)
                            .multiply(new BigDecimal(Math.abs(quantityBeforeOperation)));
                    account.setProfit(financialBeforeResult.add(shortCloseProfit));
                }
            } else {
                int closingQuantity = quantityBeforeOperation - operation.getQuantity();
                BigDecimal averagePrice = priceBeforeOperation;
                BigDecimal sellPrice = operation.getPrice();
                BigDecimal closeProfit = sellPrice.subtract(averagePrice).multiply(new BigDecimal(closingQuantity));
                account.setProfit(financialBeforeResult.add(closeProfit));
            }
            if (account.getQuantity() != 0){
                BigDecimal price = account.getTotalValue()
                        .divide(new BigDecimal(account.getQuantity().intValue()),4 , BigDecimal.ROUND_HALF_UP).abs();
                account.setPrice(price);
            } else {
                account.setTotalValue(new BigDecimal("0.0000"));
                account.setPrice(new BigDecimal("0.0000"));
            }

        } else {
            throw new St_AccountException("Operation type is not correct");
        }
        return accountRepository.save(account);
    }

    @Override
    public St_CurrencyAccount getCurrencyAccountById(Long accountId, Long userId) throws St_AccountNotFoundException {
        AccountOuterClass.GetAccountRequest request = AccountOuterClass.GetAccountRequest.newBuilder()
                .setAccountId(accountId)
                .setUserId(userId)
                .build();

        try {
            AccountOuterClass.GetAccountResponse response = this.stub.getAccountInfo(request);
            St_CurrencyAccount currencyAccount = new St_CurrencyAccount();
            currencyAccount.setAccountId(response.getAccount().getId());
            currencyAccount.setArchive(response.getAccount().getArchive());
            currencyAccount.setIsoCode(response.getAccount().getCurrency().getCurrencyCode());
            currencyAccount.setBalance(new BigDecimal(response.getAccount().getBalance()));
            currencyAccount.setName(response.getAccount().getName());
            currencyAccount.setType(response.getAccount().getType());
            return currencyAccount;
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new St_AccountNotFoundException("Account with ID " + accountId + " not found.");
            } else if (e.getStatus().getCode() == Status.Code.INVALID_ARGUMENT) {
                throw new IllegalArgumentException("Invalid request parameters.");
            } else {
                throw new RuntimeException("Unexpected error: " + e.getStatus().getDescription());
            }
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred.", e);
        }
    }

    @Autowired
    public void setAccountRepository(St_AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
