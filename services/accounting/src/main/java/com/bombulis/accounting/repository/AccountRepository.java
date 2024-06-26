package com.bombulis.accounting.repository;

import com.bombulis.accounting.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository<T extends Account> extends JpaRepository<T, Long> {
    T findAccountByIdAndUserUserIdAndDeletedFalse(Long accountId, Long userId);
    T findAccountByIdAndUserUserId(Long accountId, Long userId);
    CurrencyAccount findCurrencyAccountByIdAndUserUserIdAndDeletedFalse(Long accountId, Long userId);
    List<T> findAccountsByUserUserIdAndDeletedFalse(Long userId);
    List<T> findByUserUserIdAndDeletedFalse(Long userId);
    List<WithdrawalDestination> findWithdrawalDestinationsByUserUserIdAndDeletedFalse(Long userId);
    List<FinancingSource> findFinancingSourcesByUserUserIdAndDeletedFalse(Long userId);
    SecurityPositionAccount findSecurityPositionAccountByUserUserIdAndDeletedFalseAndTicker(Long userId, String ticker);

}
