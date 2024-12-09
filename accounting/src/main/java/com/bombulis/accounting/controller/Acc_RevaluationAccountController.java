package com.bombulis.accounting.controller;

import com.bombulis.accounting.component.Acc_MultiAuthToken;
import com.bombulis.accounting.dto.Acc_ResponseAccountDTO;
import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.entity.Acc_Currency;
import com.bombulis.accounting.service.AccountService.Acc_AccountService;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import com.bombulis.accounting.service.AccountService.exception.Acc_ServerDataAssetsException;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyNonFound;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyService;
import com.bombulis.accounting.service.RevaluationService.Acc_CurrencyRateException;
import com.bombulis.accounting.service.RevaluationService.Acc_RevaluationAccountService;
import com.bombulis.accounting.service.TransactionService.exception.Acc_CurrencyMismatchException;
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
public class Acc_RevaluationAccountController {

    private Acc_RevaluationAccountService revaluationAccountService;
    private Acc_AccountService accountService;
    private Acc_CurrencyService currencyService;

    @RequestMapping(value = "/currencies/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRevaluationBalances(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate,
                                                    @RequestParam(name = "currency") String currencyStr,
                                                    Authentication authentication) throws Acc_AccountNonFound, Acc_CurrencyNonFound, Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException, Acc_AccountOtherType, Acc_ServerDataAssetsException, Acc_CurrencyRateException {
        Map<String, Object> response = new HashMap<>();
        Acc_Currency currency = currencyService.findCurrency(currencyStr);
        Date date = new Date();
        if (localDate != null){
             date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        List<Acc_Account> accountList = (List<Acc_Account>) accountService.findUserAccounts(((Acc_MultiAuthToken) authentication).getUserId());
        response.put("balances", revaluationAccountService.getRevaluationBalanceAccountList(accountList,currency,date));
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRevaluationAccounts(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate,
                                                    @RequestParam(name = "currency") String currency,
                                                    Authentication authentication) throws Acc_AccountNonFound, Acc_CurrencyNonFound, Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException, Acc_AccountOtherType, Acc_ServerDataAssetsException, Acc_CurrencyRateException {
        Map<String, Object> response = new HashMap<>();
        Date date = new Date();
        if (localDate != null){
            date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        List<Acc_Account> accountList = (List<Acc_Account>) accountService.findUserAccounts(((Acc_MultiAuthToken) authentication).getUserId());
        response.put("accounts", revaluationAccountService.getRevaluationAccountList(accountList,currency,date)
                .stream().map(i -> new Acc_ResponseAccountDTO(i)).collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }


    @Autowired
    public void setRevaluationAccountService(Acc_RevaluationAccountService revaluationAccountService) {
        this.revaluationAccountService = revaluationAccountService;
    }

    @Autowired
    public void setAccountService(Acc_AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setCurrencyService(Acc_CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
}
