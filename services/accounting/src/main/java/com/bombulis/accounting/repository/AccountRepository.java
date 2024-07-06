package com.bombulis.accounting.repository;

import com.bombulis.accounting.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByIdAndUserUserIdAndDeletedFalse(Long accountId, Long userId);
    Optional<Account> findAccountByIdAndUserUserId(Long accountId, Long userId);
    CurrencyAccount findCurrencyAccountByIdAndUserUserIdAndDeletedFalse(Long accountId, Long userId);
    List<Account> findAccountsByUserUserIdAndDeletedFalse(Long userId);
    List<Account> findByUserUserIdAndDeletedFalse(Long userId);
    List<WithdrawalDestination> findWithdrawalDestinationsByUserUserIdAndDeletedFalse(Long userId);
    List<FinancingSource> findFinancingSourcesByUserUserIdAndDeletedFalse(Long userId);
    SecurityPositionAccount findSecurityPositionAccountByUserUserIdAndDeletedFalseAndTicker(Long userId, String ticker);

}
