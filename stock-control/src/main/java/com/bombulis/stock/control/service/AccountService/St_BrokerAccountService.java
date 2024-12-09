package com.bombulis.stock.control.service.AccountService;

import com.bombulis.stock.control.entity.St_Broker;
import com.bombulis.stock.control.model.St_BrokerAccountDTO;

import java.util.List;

public interface St_BrokerAccountService {
    St_Broker createBrokerAccount(St_BrokerAccountDTO brokerAccountDTO,
                                  Long userId);

    void deleteBrokerAccount(Long brokerId,
                                  Long userId) throws St_AccountNotFoundException;

    St_Broker getBrokerAccount(Long brokerId,
                               Long userId) throws St_AccountNotFoundException;

    List<St_Broker> getUserBrokerAccounts(Long userId);
}
