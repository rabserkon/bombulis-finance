package com.bombulis.accounting.controller;


import com.bombulis.accounting.component.MultiAuthToken;
import com.bombulis.accounting.dto.ResponseTransaction;
import com.bombulis.accounting.entity.TransactionAccount;
import com.bombulis.accounting.dto.TransactionDTO;
import com.bombulis.accounting.service.AccountService.exception.AccountException;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.CurrencyMismatchException;
import com.bombulis.accounting.service.TransactionService.SearchCriteria;
import com.bombulis.accounting.service.TransactionService.TransactionBalanceService;
import com.bombulis.accounting.service.TransactionService.TransactionService;
import com.bombulis.accounting.service.UserService.UserException;
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
public class TransactionController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private TransactionService transactionService;
    private TransactionBalanceService transactionBalanceService;

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
        SearchCriteria criteria = new SearchCriteria(from, to );
        response.put("transactions", transactionService.findAllUserTransactions(((MultiAuthToken) authentication).getUserId(), criteria)
                .stream().map(i -> new ResponseTransaction(i)).collect(Collectors.toList()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDepositTransaction(@Valid @RequestBody TransactionDTO transactionDTO,
                                                      Authentication authentication) throws CurrencyMismatchException, AccountTypeMismatchException, AccountNonFound, UserException {
        Map<String, Object> response = new HashMap<>();
        TransactionAccount transactionAccount = transactionBalanceService.createTransaction(
                transactionDTO,
                ((MultiAuthToken) authentication).getUserId());
        response.put("status","Deposit transaction created successfully");
        response.put("transaction", new ResponseTransaction(transactionAccount));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/withdrawal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createWithdrawalTransaction(@Valid @RequestBody TransactionDTO transactionDTO,
                                                         Authentication authentication) throws CurrencyMismatchException, AccountTypeMismatchException, AccountNonFound, UserException {
        Map<String, Object> response = new HashMap<>();
        TransactionAccount transactionAccount = transactionBalanceService.createTransaction(
                transactionDTO,
                ((MultiAuthToken) authentication).getUserId());
        response.put("status","Deposit transaction created successfully");
        response.put("transaction", new ResponseTransaction(transactionAccount));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO,
                                               Authentication authentication) throws CurrencyMismatchException, AccountTypeMismatchException, AccountNonFound, UserException {
        Map<String, Object> response = new HashMap<>();
        TransactionAccount transactionAccount = transactionBalanceService.createTransaction(
                transactionDTO,
                ((MultiAuthToken) authentication).getUserId());
        response.put("status","Transaction created successfully");
        response.put("transaction", new ResponseTransaction(transactionAccount));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/calculate-balance", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> calculateBalance(@RequestParam(name = "accountId") Long accountId,
                                               Authentication authentication) throws AccountException {
        Map<String, Object> response = new HashMap<>();
        response.put("status","Calculate balance for account: " + accountId);
        response.put("balance", transactionBalanceService.calculateBalance(accountId, ((MultiAuthToken) authentication).getUserId()));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTransaction() {
        return new ResponseEntity<>("Transaction updated successfully", HttpStatus.OK);
    }

    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Autowired
    public void setTransactionBalanceService(TransactionBalanceService transactionBalanceService) {
        this.transactionBalanceService = transactionBalanceService;
    }
}
