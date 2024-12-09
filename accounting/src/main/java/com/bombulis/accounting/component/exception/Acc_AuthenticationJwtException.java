package com.bombulis.accounting.component.exception;

import lombok.Getter;

public class Acc_AuthenticationJwtException extends Exception {
    @Getter
    private String message;

    public Acc_AuthenticationJwtException(String message) {
        super(message);
        this.message = message;
    }
}
