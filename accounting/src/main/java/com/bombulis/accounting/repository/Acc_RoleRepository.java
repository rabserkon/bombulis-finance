package com.bombulis.accounting.repository;

import com.bombulis.accounting.entity.Acc_Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Acc_RoleRepository extends JpaRepository<Acc_Role, Long> {
}
