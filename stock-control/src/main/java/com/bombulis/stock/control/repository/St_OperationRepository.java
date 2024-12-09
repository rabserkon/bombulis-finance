package com.bombulis.stock.control.repository;

import com.bombulis.stock.control.entity.St_Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface St_OperationRepository extends JpaRepository<St_Operation, Long> {

}
