package com.bombulis.accounting.controller.exceptionContoller;


import com.bombulis.accounting.service.AccountService.exception.AccountException;
import com.bombulis.accounting.service.AccountService.exception.ServerDataAssetsException;
import com.bombulis.accounting.service.CurrencyService.CurrencyException;
import com.bombulis.accounting.service.UserService.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AccountExceptionHandler {

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<Map<String, String>> handleValidationAccountTypeMismatchException(AccountException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundUser(UserException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(CurrencyException.class)
    public ResponseEntity<Map<String, String>> handleCurrencyException(CurrencyException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ServerDataAssetsException.class)
    public ResponseEntity<Map<String, String>> handleServerDataAssetsException(ServerDataAssetsException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "The server providing financial information is not responding");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
