package com.bombulis.stock.control.repository;

import com.bombulis.stock.control.entity.St_Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface St_AccountRepository extends JpaRepository<St_Account, Long> {
    List<St_Account> findSt_AccountByBrokerBrokerAccountIdAndUserId(Long brokerId, Long userId);

    Optional<St_Account> findSt_AccountByBrokerBrokerAccountIdAndIsinAndUserId(Long brokerId, String sIsin, Long userId);
}
