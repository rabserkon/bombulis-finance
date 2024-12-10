package com.bombulis.accounting.controller;


import com.bombulis.accounting.component.Acc_MultiAuthToken;
import com.bombulis.accounting.dto.Acc_ResponseTransaction;
import com.bombulis.accounting.dto.Acc_TransactionDTO;
import com.bombulis.accounting.entity.Acc_TransactionAccount;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountException;
import com.bombulis.accounting.service.TransactionService.Acc_SearchCriteria;
import com.bombulis.accounting.service.TransactionService.Acc_TransactionBalanceService;
import com.bombulis.accounting.service.TransactionService.Acc_TransactionService;
import com.bombulis.accounting.service.UserService.Acc_UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/transaction")
public class Acc_TransactionController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Acc_TransactionService transactionService;
    private Acc_TransactionBalanceService transactionBalanceService;

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllUserTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication authentication
    ) {
        Map<String, Object> response = new HashMap<>();

        if (to == null) {
            to = LocalDate.now().plusDays(1);
        }
        if (from == null) {
            from = to.minusMonths(1).withDayOfMonth(1);
        }
        Acc_SearchCriteria criteria = new Acc_SearchCriteria(from, to );
        response.put("transactions", transactionService.findAllUserTransactions(((Acc_MultiAuthToken) authentication).getUserId(), criteria)
                .stream().map(i -> new Acc_ResponseTransaction(i)).collect(Collectors.toList()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDepositTransaction(@Valid @RequestBody Acc_TransactionDTO transactionDTO,
                                                      Authentication authentication) throws Acc_AccountException, Acc_UserException {
        Map<String, Object> response = new HashMap<>();
        Acc_TransactionAccount transactionAccount = transactionBalanceService.createTransaction(
                transactionDTO,
                ((Acc_MultiAuthToken) authentication).getUserId());
        response.put("status","Deposit transaction created successfully");
        response.put("transaction", new Acc_ResponseTransaction(transactionAccount));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/withdrawal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createWithdrawalTransaction(@Valid @RequestBody Acc_TransactionDTO transactionDTO,
                                                         Authentication authentication) throws Acc_AccountException, Acc_UserException {
        Map<String, Object> response = new HashMap<>();
        Acc_TransactionAccount transactionAccount = transactionBalanceService.createTransaction(
                transactionDTO,
                ((Acc_MultiAuthToken) authentication).getUserId());
        response.put("status","Deposit transaction created successfully");
        response.put("transaction", new Acc_ResponseTransaction(transactionAccount));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTransaction(@Valid @RequestBody Acc_TransactionDTO transactionDTO,
                                               Authentication authentication) throws Acc_AccountException, Acc_UserException {
        Map<String, Object> response = new HashMap<>();
        Acc_TransactionAccount transactionAccount = transactionBalanceService.createTransaction(
                transactionDTO,
                ((Acc_MultiAuthToken) authentication).getUserId());
        response.put("status","Transaction created successfully");
        response.put("transaction", new Acc_ResponseTransaction(transactionAccount));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/calculate-balance", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> calculateBalance(@RequestParam(name = "accountId") Long accountId,
                                               Authentication authentication) throws Acc_AccountException {
        Map<String, Object> response = new HashMap<>();
        response.put("status","Calculate balance for account: " + accountId);
        response.put("balance", transactionBalanceService.calculateBalance(accountId, ((Acc_MultiAuthToken) authentication).getUserId()));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTransaction() {
        return new ResponseEntity<>("Transaction updated successfully", HttpStatus.OK);
    }

    @Autowired
    public void setTransactionService(Acc_TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Autowired
    public void setTransactionBalanceService(Acc_TransactionBalanceService transactionBalanceService) {
        this.transactionBalanceService = transactionBalanceService;
    }
}
