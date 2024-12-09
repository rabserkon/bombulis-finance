package com.social.network.authentication.module.service.CredentialRecoveryService;

public class TokenException extends Exception{
    public TokenException() {
    }

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
