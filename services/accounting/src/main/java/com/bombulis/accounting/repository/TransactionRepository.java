package com.bombulis.accounting.repository;

import com.bombulis.accounting.entity.TransactionAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionAccount, Long> {

    List<? extends TransactionAccount> findTransactionAccountsBySenderAccountIdOrRecipientAccountId(Long senderAccountId, Long recipientAccountId);
    List<? extends TransactionAccount> findTransactionAccountsBySenderAccountId(Long senderAccountId);
    List<? extends TransactionAccount> findTransactionAccountsByRecipientAccountId( Long recipientAccountId);
}
