package com.bombulis.accounting.controller;

import com.bombulis.accounting.component.MultiAuthToken;
import com.bombulis.accounting.dto.AccountEditDTO;
import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.entity.CurrencyAccount;
import com.bombulis.accounting.dto.AccountDTO;
import com.bombulis.accounting.service.AccountService.AccountType;
import com.bombulis.accounting.service.AccountService.*;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.CurrencyService.CurrencyNonFound;
import com.bombulis.accounting.service.TransactionService.exception.CurrencyMismatchException;
import com.bombulis.accounting.service.TransactionService.TransactionBalanceService;
import com.bombulis.accounting.service.UserService.NotFoundUser;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RequestMapping("/accounts")
public class AccountController {

    private AccountService currencyAccountService;
    private TransactionBalanceService transactionService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @SneakyThrows
    @RequestMapping(value = "/сonsolidated", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAccount(@RequestParam(name = "currency") String currency,
                                           Authentication authentication){
        Map<String, Object> response = new HashMap<>();
        List<Account> accounts = (List<Account>) currencyAccountService.findUserAccounts( ((MultiAuthToken) authentication).getUserId());
        response.put("accounts", accounts);
        return ResponseEntity.ok(response);

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAccount(@Valid @RequestBody AccountDTO accountDTO,
                                           Authentication authentication) throws CurrencyNonFound, NotFoundUser, AccountOtherType {
        Account account = currencyAccountService.createAccount(accountDTO, ((MultiAuthToken) authentication).getUserId());
        return ResponseEntity.ok(account);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editAccount(@Valid @RequestBody AccountEditDTO accountDTO,
                                         Authentication authentication) throws CurrencyNonFound {

        Account account = currencyAccountService.editAccount(accountDTO, ((MultiAuthToken) authentication).getUserId());
        return ResponseEntity.ok(account);

    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccountsInformation(Authentication authentication) throws AccountNonFound {
        Map<String, Object> response = new HashMap<>();
        List<Account> accounts = (List<Account>) currencyAccountService.findUserAccounts( ((MultiAuthToken) authentication).getUserId());
        response.put("accounts", accounts);
        return ResponseEntity.ok(response);

    }

    @RequestMapping(value = "/information", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccountInformation(@RequestParam(name = "accountId") Long accountId,
                                                   Authentication authentication) throws AccountTypeMismatchException, AccountNonFound {
        Map<String, Object> response = new HashMap<>();
        CurrencyAccount account = currencyAccountService.findAccount(accountId, ((MultiAuthToken) authentication).getUserId());
        response.put("account", account);
        return ResponseEntity.ok(response);

    }

    @RequestMapping(value = "/balance-reconciliation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> balanceReconciliation(@RequestParam(name = "accountId") Long accountId,
                                                   Authentication authentication) throws AccountTypeMismatchException, AccountNonFound, AccountOtherType, CurrencyMismatchException {
        Map<String, Object> response = new HashMap<>();
        BalanceDTO balanceDTO = transactionService.balanceReconciliation(accountId, ((MultiAuthToken) authentication).getUserId());
        //response.put("account", account);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> accountDelete(@RequestParam(name = "accountId") Long accountId,
                                                   Authentication authentication) throws AccountNonFound {
        Map<String, Object> response = new HashMap<>();
        CurrencyAccount account = currencyAccountService.deleteAccount(accountId, ((MultiAuthToken) authentication).getUserId());
        response.put("delete", true);
        response.put("account", account);
        return ResponseEntity.ok(response);

    }

    @RequestMapping(value = "/types", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AccountTypeDTO> getAccountTypes() {
        return Arrays.stream(AccountType.values())
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
    public void setTransactionService(TransactionBalanceService transactionService) {
        this.transactionService = transactionService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.currencyAccountService = accountService;
    }


}
