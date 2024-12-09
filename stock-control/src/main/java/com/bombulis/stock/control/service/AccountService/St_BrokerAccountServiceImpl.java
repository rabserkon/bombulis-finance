package com.bombulis.stock.control.service.AccountService;

import com.bombulis.stock.control.entity.St_Broker;
import com.bombulis.stock.control.model.St_BrokerAccountDTO;
import com.bombulis.stock.control.repository.St_BrokerAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class St_BrokerAccountServiceImpl implements St_BrokerAccountService {

    public St_BrokerAccountRepository st_brokerAccountRepository;

    @Override
    public St_Broker createBrokerAccount(St_BrokerAccountDTO brokerAccountDTO,
                                         Long userId){
        St_Broker broker = new St_Broker();
        broker.setName(brokerAccountDTO.getName());
        broker.setUserId(userId);
        broker.setCountry(brokerAccountDTO.getCountry());
        return st_brokerAccountRepository.save(broker);
    }

    @Override
    public void deleteBrokerAccount(Long brokerId,
                                         Long userId) throws St_AccountNotFoundException {
        St_Broker broker = st_brokerAccountRepository.findSt_BrokerByUserIdAndBrokerAccountIdAndDeletedFalse(userId, brokerId)
                .orElseThrow(() -> new St_AccountNotFoundException("Not found broker account"));
        broker.setDeleted(true);
        st_brokerAccountRepository.save(broker);
    }

    @Override
    public St_Broker getBrokerAccount(Long brokerId,
                                         Long userId) throws St_AccountNotFoundException {
        St_Broker broker = st_brokerAccountRepository.findSt_BrokerByUserIdAndBrokerAccountIdAndDeletedFalse(userId, brokerId)
                .orElseThrow(() -> new St_AccountNotFoundException("Not found broker account"));
        return broker;
    }

    @Override
    public List<St_Broker> getUserBrokerAccounts(Long userId){
        List<St_Broker> brokerList = st_brokerAccountRepository.findSt_BrokerByUserIdAndDeletedFalse(userId);
        return brokerList;
    }


    @Autowired
    public void setSt_brokerAccountRepository(St_BrokerAccountRepository st_brokerAccountRepository) {
        this.st_brokerAccountRepository = st_brokerAccountRepository;
    }
}
