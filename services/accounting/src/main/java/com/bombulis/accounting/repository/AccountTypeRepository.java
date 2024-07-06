package com.bombulis.accounting.repository;

import com.bombulis.accounting.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
    List<AccountType> findAll();
    List<AccountType> findAccountTypeByArchivalFalse();

}
