package com.social.network.authentication.module.service.CredentialRecoveryService;

public class UserAccountException extends Exception {
    public UserAccountException() {
    }

    public UserAccountException(String message) {
        super(message);
    }

    public UserAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
