package com.social.network.authentication.module.component.exception;

import com.social.network.authentication.module.service.CredentialRecoveryService.TokenException;

public class CodeTokenCreateException extends TokenException {
    public CodeTokenCreateException(String message) {
        super(message);
    }
}
