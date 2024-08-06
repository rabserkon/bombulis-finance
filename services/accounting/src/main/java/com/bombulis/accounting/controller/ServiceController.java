package com.bombulis.accounting.controller;

import com.bombulis.accounting.component.MultiAuthToken;
import com.bombulis.accounting.dto.CurrencyDTO;
import com.bombulis.accounting.dto.ResponseAccountDTO;
import com.bombulis.accounting.dto.ResponseTransaction;
import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.entity.AccountType;
import com.bombulis.accounting.service.AccountService.AccountService;
import com.bombulis.accounting.service.AccountService.AccountTypeService;
import com.bombulis.accounting.service.AccountService.FinancingSourceService;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.AccountService.exception.ServerDataAssetsException;
import com.bombulis.accounting.service.CurrencyService.CurrencyNonFound;
import com.bombulis.accounting.service.CurrencyService.CurrencyService;
import com.bombulis.accounting.service.RevaluationService.ExchangeRateService;
import com.bombulis.accounting.service.RevaluationService.RevaluationAccountService;
import com.bombulis.accounting.service.TransactionService.SearchCriteria;
import com.bombulis.accounting.service.TransactionService.TransactionService;
import com.bombulis.accounting.service.TransactionService.exception.CurrencyMismatchException;
import com.bombulis.accounting.service.UserService.NotFoundUser;
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
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/service")
public class ServiceController {

    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private ExchangeRateService rateService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountTypeService accountTypeService;
    @Autowired
    private RevaluationAccountService revaluationAccountService;
    @Autowired
    private FinancingSourceService sourcesService;

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
        MultiAuthToken authToken = (MultiAuthToken) auth;
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
    ) throws CurrencyNonFound, ServerDataAssetsException, AccountNonFound, CurrencyMismatchException, AccountTypeMismatchException, AccountOtherType, NotFoundUser {
        MultiAuthToken authToken = ((MultiAuthToken) authentication);
        Map<String, Object> response = new HashMap<>();
        List<Account> accountList = (List<Account>) accountService.findUserAccounts(authToken.getUserId());
        response.put("currencies", currencyService.getAllCurrencies().stream()
                .map(i -> new CurrencyDTO(i)).collect(Collectors.toList()));
        response.put("currencies_rates", rateService.getExchangeRate(
                currencyService.getAllCurrencies(),
                currencyService.getAllCurrencies(),
                new Date()
        ));
        /*response.put("revaluation_account", revaluationAccountService.getRevaluationAccountList(accountList,
                "USD",
                new Date()));*/
        response.put("transactional_list", transactionService.findAllUserTransactions(authToken.getUserId(),
                new SearchCriteria(LocalDate.now().minusDays(LocalDate.now().getDayOfMonth()), LocalDate.now()))
                .stream().map(i -> new ResponseTransaction(i)).collect(Collectors.toList()));
        response.put("account_list", accountList
                .stream().map(i -> new ResponseAccountDTO(i)).collect(Collectors.toList()));
        response.put("account_types", accountTypeService.getAllTypes().stream().map(i -> new AccountTypeDTO(i))
                .collect(Collectors.toList()));
        response.put("source_list",sourcesService.getListDepositAccount(authToken.getUserId())
                .stream().map(i -> new ResponseAccountDTO(i))
                .collect(Collectors.toList()));
        response.put("withdrawal_list", sourcesService.getListWithdrawalAccount(authToken.getUserId())
                .stream().map(i -> new ResponseAccountDTO(i))
                .collect(Collectors.toList()));
        return ResponseEntity.ok().body(response);
    }

    private class AccountTypeDTO {
        @Getter @Setter
        private String name;
        @Getter @Setter
        private String type;

        public AccountTypeDTO(AccountType accountType) {
            this.name = accountType.getName();
            this.type = accountType.getType();
        }
    }
}
