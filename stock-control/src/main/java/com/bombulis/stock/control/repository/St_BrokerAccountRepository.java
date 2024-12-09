package com.bombulis.stock.control.repository;

import com.bombulis.stock.control.entity.St_Broker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface St_BrokerAccountRepository extends CrudRepository<St_Broker, Long> {
    List<St_Broker> findSt_BrokerByUserId(Long userId);
    List<St_Broker> findSt_BrokerByUserIdAndDeletedFalse(Long userId);
    Optional<St_Broker> findSt_BrokerByUserIdAndBrokerAccountIdAndDeletedFalse(Long userId, Long brokerId);
}
