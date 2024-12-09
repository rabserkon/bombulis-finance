package com.bombulis.accounting.repository;

import com.bombulis.accounting.entity.Acc_Transaction;
import com.bombulis.accounting.entity.Acc_TransactionAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Acc_TransactionRepository extends JpaRepository<Acc_Transaction, Long> {
    List<? extends Acc_TransactionAccount> findTransactionAccountsBySenderAccountIdOrRecipientAccountId(Long senderAccountId, Long recipientAccountId);
    List<? extends Acc_TransactionAccount> findTransactionAccountsBySenderAccountId(Long senderAccountId);
    List<? extends Acc_TransactionAccount> findTransactionAccountsByRecipientAccountId(Long recipientAccountId);
}
