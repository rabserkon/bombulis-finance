package com.social.network.authentication.module.service.RegistrationService;

import com.social.network.authentication.module.component.exception.CodeTokenCreateException;
import com.social.network.authentication.module.dto.ResponseRegistrationDTO;
import com.social.network.authentication.module.dto.UserDTO;
import com.social.network.authentication.module.entity.CodeToken;
import com.social.network.authentication.module.entity.User;
import com.social.network.authentication.module.service.CredentialRecoveryService.TokenException;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

public interface RegistrationService {

    User registrationUser(UserDTO userDTO) throws RegistrationException;
    ResponseRegistrationDTO registrationUserAndConfirmToken(UserDTO userDTO) throws RegistrationException;
    RegistrationTokenDTO createRegistrationConfirmToken(User user) throws MessagingException, AuthenticationFailedException;
    boolean confirmRegistrationTokenByCode(String loginOrEmail, String code);
    boolean confirmRegistrationTokenByToken(String token, String code) throws CodeTokenCreateException, TokenException;
}
