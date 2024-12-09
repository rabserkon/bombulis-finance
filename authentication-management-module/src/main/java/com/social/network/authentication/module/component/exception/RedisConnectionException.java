package com.social.network.authentication.module.component.exception;

public class RedisConnectionException extends Exception {
    public RedisConnectionException() {
        super("Service not work at now moment");
    }
}
