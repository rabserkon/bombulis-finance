package com.bombulis.accounting.component.exception;

import lombok.Getter;

public class AuthenticationJwtException extends Exception {
    @Getter
    private String message;

    public AuthenticationJwtException(String message) {
        super(message);
        this.message = message;
    }
}
