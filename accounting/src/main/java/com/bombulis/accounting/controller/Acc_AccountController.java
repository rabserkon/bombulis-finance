package com.bombulis.accounting.controller;

import com.bombulis.accounting.component.Acc_MultiAuthToken;
import com.bombulis.accounting.dto.Acc_AccountEditDTO;
import com.bombulis.accounting.dto.Acc_BalanceDTO;
import com.bombulis.accounting.dto.Acc_ResponseAccountDTO;
import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.dto.Acc_AccountDTO;
import com.bombulis.accounting.service.AccountService.Acc_AccountType;
import com.bombulis.accounting.service.AccountService.*;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountException;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyNonFound;
import com.bombulis.accounting.service.TransactionService.exception.Acc_CurrencyMismatchException;
import com.bombulis.accounting.service.TransactionService.Acc_TransactionBalanceService;
import com.bombulis.accounting.service.UserService.Acc_UserException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/accounts")
public class Acc_AccountController {

    private Acc_AccountService accountService;
    private Acc_TransactionBalanceService transactionService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @SneakyThrows
    @RequestMapping(value = "/сonsolidated", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAccount(@RequestParam(name = "currency") String currency,
                                           Authentication authentication){
        Map<String, Object> response = new HashMap<>();
        List<Acc_Account> accounts = (List<Acc_Account>) accountService.findUserAccounts( ((Acc_MultiAuthToken) authentication).getUserId());
        response.put("accounts", accounts.stream().map(i -> new Acc_ResponseAccountDTO(i)).collect(Collectors.toList()));
        return ResponseEntity.ok(response);

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAccount(@Valid @RequestBody Acc_AccountDTO accountDTO,
                                           Authentication authentication) throws Acc_CurrencyNonFound, Acc_UserException, Acc_AccountException {
        Acc_Account account = accountService.createAccount(accountDTO, ((Acc_MultiAuthToken) authentication).getUserId());
        return ResponseEntity.ok(new Acc_ResponseAccountDTO(account));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editAccount(@Valid @RequestBody Acc_AccountEditDTO accountDTO,
                                         Authentication authentication) throws Acc_CurrencyNonFound, Acc_AccountNonFound {
        Acc_Account account = accountService.editAccount(accountDTO, ((Acc_MultiAuthToken) authentication).getUserId());
        return ResponseEntity.ok(new Acc_ResponseAccountDTO(account));

    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccountsInformation(Authentication authentication) throws Acc_AccountNonFound {
        Map<String, Object> response = new HashMap<>();
        List<Acc_Account> accounts = (List<Acc_Account>) accountService.findUserAccounts( ((Acc_MultiAuthToken) authentication).getUserId());
        response.put("accounts", accounts.stream().map(account -> new Acc_ResponseAccountDTO(account))
                .collect(Collectors.toList()));
        return ResponseEntity.ok(response);

    }

    @RequestMapping(value = "/information", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccountInformation(@RequestParam(name = "accountId") Long accountId,
                                                   Authentication authentication) throws Acc_AccountTypeMismatchException, Acc_AccountNonFound {
        Map<String, Object> response = new HashMap<>();
        Acc_Account account = accountService
                .findAccount(accountId, ((Acc_MultiAuthToken) authentication)
                        .getUserId());
        response.put("account", new Acc_ResponseAccountDTO(account));
        return ResponseEntity.ok(response);

    }

    @RequestMapping(value = "/balance-reconciliation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> balanceReconciliation(@RequestParam(name = "accountId") Long accountId,
                                                   Authentication authentication) throws Acc_AccountTypeMismatchException, Acc_AccountNonFound, Acc_AccountOtherType, Acc_CurrencyMismatchException {
        Map<String, Object> response = new HashMap<>();
        Acc_BalanceDTO balanceDTO = transactionService
                .balanceReconciliation(accountId, ((Acc_MultiAuthToken) authentication)
                        .getUserId());
        response.put("account_balance", balanceDTO);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> accountDelete(@RequestParam(name = "accountId") Long accountId,
                                                   Authentication authentication) throws Acc_AccountException {
        Map<String, Object> response = new HashMap<>();
        Acc_Account account = accountService.deleteAccount(accountId, ((Acc_MultiAuthToken) authentication).getUserId());
        response.put("delete", true);
        response.put("account", new Acc_ResponseAccountDTO(account));
        return ResponseEntity.ok(response);

    }

    @RequestMapping(value = "/types", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AccountTypeDTO> getAccountTypes() {
        return Arrays.stream(Acc_AccountType.values())
                .map(accountType -> new AccountTypeDTO(accountType.getType(), accountType.name()))
                .collect(Collectors.toList());
    }

    static class AccountTypeDTO {
        private final String name;
        private final String type;

        public AccountTypeDTO(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }

    @RequestMapping(value = "/configure", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> configureAccount(@RequestParam(name = "accountId") Long accountId){
        return null;
    }

    @Autowired
    public void setTransactionService(Acc_TransactionBalanceService transactionService) {
        this.transactionService = transactionService;
    }

    @Autowired
    public void setAccountService(Acc_AccountService accountService) {
        this.accountService = accountService;
    }


}
