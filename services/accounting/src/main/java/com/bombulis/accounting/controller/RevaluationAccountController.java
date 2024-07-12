package com.bombulis.accounting.controller;

import com.bombulis.accounting.component.MultiAuthToken;
import com.bombulis.accounting.dto.ResponseAccountDTO;
import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.entity.Currency;
import com.bombulis.accounting.service.AccountService.AccountService;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.AccountService.exception.ServerDataAssetsException;
import com.bombulis.accounting.service.CurrencyService.CurrencyNonFound;
import com.bombulis.accounting.service.CurrencyService.CurrencyService;
import com.bombulis.accounting.service.RevaluationService.CurrencyRateException;
import com.bombulis.accounting.service.RevaluationService.RevaluationAccountService;
import com.bombulis.accounting.service.TransactionService.exception.CurrencyMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/revaluations")
public class RevaluationAccountController {

    private RevaluationAccountService revaluationAccountService;
    private AccountService accountService;
    private CurrencyService currencyService;

    @RequestMapping(value = "/currencies/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRevaluationBalances(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate,
                                                    @RequestParam(name = "currency") String currencyStr,
                                                    Authentication authentication) throws AccountNonFound, CurrencyNonFound, CurrencyMismatchException, AccountTypeMismatchException, AccountOtherType, ServerDataAssetsException, CurrencyRateException {
        Map<String, Object> response = new HashMap<>();
        Currency currency = currencyService.findCurrency(currencyStr);
        Date date = new Date();
        if (localDate != null){
             date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        List<Account> accountList = (List<Account>) accountService.findUserAccounts(((MultiAuthToken) authentication).getUserId());
        response.put("balances", revaluationAccountService.getRevaluationBalanceAccountList(accountList,currency,date));
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRevaluationAccounts(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate,
                                                    @RequestParam(name = "currency") String currency,
                                                    Authentication authentication) throws AccountNonFound, CurrencyNonFound, CurrencyMismatchException, AccountTypeMismatchException, AccountOtherType, ServerDataAssetsException, CurrencyRateException {
        Map<String, Object> response = new HashMap<>();
        Date date = new Date();
        if (localDate != null){
            date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        List<Account> accountList = (List<Account>) accountService.findUserAccounts(((MultiAuthToken) authentication).getUserId());
        response.put("accounts", revaluationAccountService.getRevaluationAccountList(accountList,currency,date)
                .stream().map(i -> new ResponseAccountDTO(i)).collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }


    @Autowired
    public void setRevaluationAccountService(RevaluationAccountService revaluationAccountService) {
        this.revaluationAccountService = revaluationAccountService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
}
