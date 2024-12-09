package com.bombulis.stock.control.component.exception;

import lombok.Getter;

public class St_AuthenticationJwtException extends Exception {
    @Getter
    private String message;

    public St_AuthenticationJwtException(String message) {
        super(message);
        this.message = message;
    }
}
