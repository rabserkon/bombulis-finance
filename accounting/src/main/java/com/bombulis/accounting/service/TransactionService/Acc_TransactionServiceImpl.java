package com.bombulis.accounting.service.TransactionService;

import com.bombulis.accounting.entity.Acc_TransactionAccount;
import com.bombulis.accounting.repository.Acc_TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class Acc_TransactionServiceImpl implements Acc_TransactionService {
    private Acc_TransactionRepository transactionRepository;
    private EntityManager entityManager;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<? extends Acc_TransactionAccount> findAllUserTransactions(Long userId) {
        List<? extends Acc_TransactionAccount> transactionList = transactionRepository.findTransactionAccountsBySenderAccountId(userId);
        if (transactionList == null){
            return new ArrayList<>();
        }
        return transactionList;
    }

    @Override
    public List<? extends Acc_TransactionAccount> findAllUserTransactions(Long userId, Acc_SearchCriteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Acc_TransactionAccount> query = cb.createQuery(Acc_TransactionAccount.class);
        Root<Acc_TransactionAccount> root = query.from(Acc_TransactionAccount.class);

        Path<Long> userIdPath = root.get("user").get("userId");
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getFrom() != null && criteria.getTo() != null) {
            Timestamp startTimestamp = Timestamp.valueOf(criteria.getFrom().atStartOfDay());
            Timestamp endTimestamp = Timestamp.valueOf(criteria.getTo().atStartOfDay().plusDays(1).minusNanos(1));
            predicates.add(cb.between(root.get("transactionDate"), startTimestamp, endTimestamp));
        }

        predicates.add(cb.equal(userIdPath, userId));
        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Autowired
    public void setTransactionRepository(Acc_TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}
