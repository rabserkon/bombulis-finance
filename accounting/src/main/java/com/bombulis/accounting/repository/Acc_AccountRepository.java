package com.bombulis.accounting.repository;

import com.bombulis.accounting.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Acc_AccountRepository extends JpaRepository<Acc_Account, Long> {
    Optional<Acc_Account> findAccountByIdAndUserUserIdAndDeletedFalse(Long accountId, Long userId);
    Optional<Acc_Account> findAccountByIdAndUserUserId(Long accountId, Long userId);
    Acc_CurrencyAccount findCurrencyAccountByIdAndUserUserIdAndDeletedFalse(Long accountId, Long userId);
    List<Acc_Account> findAccountsByUserUserIdAndDeletedFalse(Long userId);
    List<Acc_Account> findByUserUserIdAndDeletedFalse(Long userId);
    List<Acc_WithdrawalDestination> findWithdrawalDestinationsByUserUserIdAndDeletedFalse(Long userId);
    List<Acc_FinancingSource> findFinancingSourcesByUserUserIdAndDeletedFalse(Long userId);
    Acc_SecurityPositionAccount findSecurityPositionAccountByUserUserIdAndDeletedFalseAndTicker(Long userId, String ticker);

}
