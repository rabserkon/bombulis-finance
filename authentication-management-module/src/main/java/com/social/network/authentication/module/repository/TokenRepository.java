package com.social.network.authentication.module.repository;

import com.social.network.authentication.module.entity.AccountToken;
import com.social.network.authentication.module.entity.CodeToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<AccountToken,Long> {
    Optional<AccountToken> findAccountTokenByAboutTokenAndCreateTimeBeforeAndValidateTimeAfterAndStatusFalseAndTokenUUID(String aboutToken,
                                                                                                                         LocalDateTime createTime,
                                                                                                                         LocalDateTime validateTime,
                                                                                                                         String tokenUUID);

    Optional<AccountToken> findAccountTokenByAboutTokenAndCreateTimeBeforeAndValidateTimeAfterAndStatusAndTokenUUID(String aboutToken,
                                                                                                                    LocalDateTime createTime,
                                                                                                                    LocalDateTime validateTime,
                                                                                                                    boolean status,
                                                                                                                    String tokenUUID);

    Optional<CodeToken> findCodeTokenByAboutTokenAndCreateTimeBeforeAndValidateTimeAfterAndStatusAndTokenUUID(String aboutToken,
                                                                                                          LocalDateTime createTime,
                                                                                                          LocalDateTime validateTime,
                                                                                                          boolean status,
                                                                                                          String tokenUUID);



}
