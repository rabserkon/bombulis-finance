package com.bombulis.accounting.controller.exceptionContoller;


import com.bombulis.accounting.service.AccountService.exception.Acc_AccountException;
import com.bombulis.accounting.service.AccountService.exception.Acc_ServerDataAssetsException;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyException;
import com.bombulis.accounting.service.RevaluationService.Acc_CurrencyRateException;
import com.bombulis.accounting.service.UserService.Acc_UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class Acc_AccountExceptionHandler {

    @ExceptionHandler(Acc_AccountException.class)
    public ResponseEntity<Map<String, String>> handleValidationAccountTypeMismatchException(Acc_AccountException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Acc_UserException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundUser(Acc_UserException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Acc_CurrencyException.class)
    public ResponseEntity<Map<String, String>> handleCurrencyException(Acc_CurrencyException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Acc_ServerDataAssetsException.class)
    public ResponseEntity<Map<String, String>> handleServerDataAssetsException(Acc_ServerDataAssetsException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "The server providing financial information is not responding");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Acc_CurrencyRateException.class)
    public ResponseEntity<Map<String, String>> handleCurrencyRateException(Acc_CurrencyRateException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "The server providing financial information is not responding");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
