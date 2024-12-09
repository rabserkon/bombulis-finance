package com.social.network.authentication.module.repository;

import com.social.network.authentication.module.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findById(Long id);
    Optional<User> findUserByEmailOrLoginAndAccountNonLockedTrueAndEnabledTrue(String email, String login);
    Optional<User> findUserByEmailAndAccountNonLockedTrue(String email);
    User findUserByEmailOrLogin(String email, String login);
}
