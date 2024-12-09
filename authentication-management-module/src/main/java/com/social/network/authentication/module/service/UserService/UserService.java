package com.social.network.authentication.module.service.UserService;

import com.social.network.authentication.module.dto.UserDTO;
import com.social.network.authentication.module.entity.User;
import com.social.network.authentication.module.service.RegistrationService.RegistrationException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    boolean checkExistsThisUser(String email, String login) throws RegistrationException;
    User createAccount(UserDTO user) throws RegistrationException;
    void addRoles(User user, String... roles);

    @Transactional
    User setEnabledUser(User user, boolean status);
}
