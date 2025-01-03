package com.bombulis.accounting.controller;

import com.bombulis.accounting.component.Acc_MultiAuthToken;
import com.bombulis.accounting.dto.Acc_CurrencyDTO;
import com.bombulis.accounting.dto.Acc_ResponseAccountDTO;
import com.bombulis.accounting.dto.Acc_ResponseTransaction;
import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.entity.Acc_AccountType;
import com.bombulis.accounting.entity.Acc_Currency;
import com.bombulis.accounting.service.AccountService.Acc_AccountService;
import com.bombulis.accounting.service.AccountService.Acc_AccountTypeService;
import com.bombulis.accounting.service.AccountService.Acc_FinancingSourceService;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import com.bombulis.accounting.service.AccountService.exception.Acc_ServerDataAssetsException;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyNonFound;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyService;
import com.bombulis.accounting.service.RevaluationService.Acc_ExchangeRateService;
import com.bombulis.accounting.service.RevaluationService.Acc_RevaluationAccountService;
import com.bombulis.accounting.service.TransactionService.Acc_SearchCriteria;
import com.bombulis.accounting.service.TransactionService.Acc_TransactionService;
import com.bombulis.accounting.service.TransactionService.exception.Acc_CurrencyMismatchException;
import com.bombulis.accounting.service.UserService.Acc_NotFoundUser;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/service")
public class Acc_ServiceController {

    @Autowired
    private Acc_CurrencyService currencyService;
    @Autowired
    private Acc_ExchangeRateService rateService;
    @Autowired
    private Acc_AccountService accountService;
    @Autowired
    private Acc_TransactionService transactionService;
    @Autowired
    private Acc_AccountTypeService accountTypeService;
    @Autowired
    private Acc_RevaluationAccountService revaluationAccountService;
    @Autowired
    private Acc_FinancingSourceService sourcesService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public final static String SERVICE_NAME = "accounting-service";

    @RequestMapping(method = RequestMethod.GET, value = "/status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> serviceRequest(){
        Map<String, Object> response = new HashMap<>();
        LocalDateTime currentTime = LocalDateTime.now();
        response.put("status", "running");
        response.put("service", SERVICE_NAME);
        response.put("message", "Service is up and running. Current time: " + currentTime.toString());
        response.put("time", currentTime.toString());
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/auth/status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> authRequest(HttpServletRequest request,
                                          Authentication auth){
        Map<String, Object> response = new HashMap<>();
        Acc_MultiAuthToken authToken = (Acc_MultiAuthToken) auth;
        LocalDateTime currentTime = LocalDateTime.now();
        response.put("status", authToken.isAuth());
        response.put("service", SERVICE_NAME);
        response.put("userId", authToken.getUserId());
        response.put("message", "User auth status: " + authToken.isAuth());
        response.put("time", currentTime.toString() );
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/dashboard/information",
            produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> dashboardInformation(
            Authentication authentication
    ) throws Acc_CurrencyNonFound, Acc_AccountNonFound, Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException, Acc_AccountOtherType, Acc_NotFoundUser {
        Acc_MultiAuthToken authToken = ((Acc_MultiAuthToken) authentication);
        Map<String, Object> response = new HashMap<>();
        List<Acc_Account> accountList = (List<Acc_Account>) accountService.findUserAccounts(authToken.getUserId());
        response.put("currencies", currencyService.getAllCurrencies().stream()
                .map(i -> new Acc_CurrencyDTO(i)).collect(Collectors.toList()));
        try {
            response.put("currencies_rates", rateService.getExchangeRate(
                    currencyService.getAllCurrencies(),
                    currencyService.getAllCurrencies(),
                    new Date()
            ));
        } catch (Acc_ServerDataAssetsException e) {
            response.put("currencies_rates", new ArrayList<Acc_Currency>());
        }
        response.put("currencies_rates", new ArrayList<Acc_Currency>());
        /*response.put("revaluation_account", revaluationAccountService.getRevaluationAccountList(accountList,
                "USD",
                new Date()));*/
        response.put("transactional_list", transactionService.findAllUserTransactions(authToken.getUserId(),
                new Acc_SearchCriteria(LocalDate.now().minusDays(LocalDate.now().getDayOfMonth()), LocalDate.now()))
                .stream().map(i -> new Acc_ResponseTransaction(i)).collect(Collectors.toList()));
        response.put("account_list", accountList
                .stream().map(i -> new Acc_ResponseAccountDTO(i)).collect(Collectors.toList()));
        response.put("account_types", accountTypeService.getAllTypes().stream().map(i -> new AccountTypeDTO(i))
                .collect(Collectors.toList()));
        response.put("source_list",sourcesService.getListDepositAccount(authToken.getUserId())
                .stream().map(i -> new Acc_ResponseAccountDTO(i))
                .collect(Collectors.toList()));
        response.put("withdrawal_list", sourcesService.getListWithdrawalAccount(authToken.getUserId())
                .stream().map(i -> new Acc_ResponseAccountDTO(i))
                .collect(Collectors.toList()));
        return ResponseEntity.ok().body(response);
    }

    private class AccountTypeDTO {
        @Getter @Setter
        private String name;
        @Getter @Setter
        private String type;

        public AccountTypeDTO(Acc_AccountType accountType) {
            this.name = accountType.getName();
            this.type = accountType.getType();
        }
    }
}
