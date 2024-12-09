package com.social.network.authentication.module.repository;

import com.social.network.authentication.module.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {
    List<AuthToken> findByCreateTimeBeforeAndValidateTimeAfterAndStatusFalseAndUserUserId(LocalDateTime createTime, LocalDateTime validateTime, Long userId);

}
