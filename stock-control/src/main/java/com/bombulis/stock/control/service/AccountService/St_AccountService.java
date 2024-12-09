package com.bombulis.stock.control.service.AccountService;

import com.bombulis.stock.control.entity.St_Account;
import com.bombulis.stock.control.entity.St_AccountType;
import com.bombulis.stock.control.entity.St_Broker;
import com.bombulis.stock.control.entity.St_Operation;
import com.bombulis.stock.control.model.St_CurrencyAccount;
import com.bombulis.stock.control.model.St_StockMatchProfile;

import java.time.LocalDateTime;

public interface St_AccountService {
    St_Account getAccountByIdAndIsin(Long brokerID, String isin, Long userId) throws St_AccountNotFoundException;

    St_Account createAccountByIdAndIsin(Long brokerID, String isin, Long userId) throws St_AccountNotFoundException;

    St_Account updateAccountBalance(St_Account account,
                                    St_Operation operation,
                                    Long userId) throws St_AccountException;

    St_CurrencyAccount getCurrencyAccountById(Long accountId, Long userId) throws St_AccountNotFoundException;

    St_Account getOrCreateEquityAccount(String isin,
                                        Long userId,
                                        St_AccountType accountType,
                                        St_Broker broker,
                                        St_StockMatchProfile companyName,
                                        LocalDateTime date) throws St_AccountNotFoundException;

    St_Account getAccountById(Long brokerID, Long userId) throws St_AccountNotFoundException;

}
