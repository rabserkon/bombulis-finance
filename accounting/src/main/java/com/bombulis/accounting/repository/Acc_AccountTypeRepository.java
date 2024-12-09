package com.bombulis.accounting.repository;

import com.bombulis.accounting.entity.Acc_AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Acc_AccountTypeRepository extends JpaRepository<Acc_AccountType, Long> {
    List<Acc_AccountType> findAll();
    List<Acc_AccountType> findAccountTypeByArchivalFalse();

}
