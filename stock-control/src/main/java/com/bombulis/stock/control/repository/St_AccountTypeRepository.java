package com.bombulis.stock.control.repository;

import com.bombulis.stock.control.entity.St_AccountType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface St_AccountTypeRepository extends CrudRepository<St_AccountType, Long> {
    Optional<St_AccountType> findSt_AccountTypesByTypeAndActiveTrue(String type);
}
