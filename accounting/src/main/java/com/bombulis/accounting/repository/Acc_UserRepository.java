package com.bombulis.accounting.repository;

import com.bombulis.accounting.entity.Acc_User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Acc_UserRepository extends JpaRepository<Acc_User, Long> {
    Optional<Acc_User> findUserByUserId(Long userId);

}
