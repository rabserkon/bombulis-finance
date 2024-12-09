package com.bombulis.stock.control.controller;


import com.bombulis.stock.control.component.St_MultiAuthToken;
import com.bombulis.stock.control.entity.St_Broker;
import com.bombulis.stock.control.entity.St_Currency;
import com.bombulis.stock.control.model.St_BrokerAccountDTO;
import com.bombulis.stock.control.model.St_CurrencyAccount;
import com.bombulis.stock.control.service.AccountService.St_AccountException;
import com.bombulis.stock.control.service.AccountService.St_AccountNotFoundException;
import com.bombulis.stock.control.service.AccountService.St_AccountService;
import com.bombulis.stock.control.service.AccountService.St_BrokerAccountService;
import com.bombulis.stock.control.service.OperationService.St_CurrencyService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/account")
public class St_AccountController {

    @Autowired
    private St_AccountService accountService;

    @Autowired
    private St_BrokerAccountService brokerAccountService;

    @GetMapping("/currency/get/info")
    public Map<String,Object> availableCurrency(Authentication authentication,
                                                @RequestParam("accountId") Long accountId) throws IOException, St_AccountException {
        Map<String,Object> object = new HashMap<>();
        St_MultiAuthToken auth = ((St_MultiAuthToken) authentication);
        St_CurrencyAccount currencyAccount = accountService.getCurrencyAccountById(accountId, auth.getUserId());
        object.put("status", "success");
        object.put("account", currencyAccount);
        return object;
    }

    @PostMapping(value = "/broker/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Object> createBrokerAccount(Authentication authentication,
                                                  @Valid @RequestBody St_BrokerAccountDTO brokerAccount) throws IOException, St_AccountException {
        Map<String,Object> object = new HashMap<>();
        St_MultiAuthToken auth = ((St_MultiAuthToken) authentication);
        St_Broker broker = brokerAccountService.createBrokerAccount(brokerAccount,auth.getUserId());
        object.put("brokerAccount", new St_BrokerAccountDTO(broker));
        return object;
    }


    @GetMapping("/broker/get")
    public Map<String,Object> getBrokerAccount(Authentication authentication,
                                               @RequestParam("accountId") Long accountId) throws IOException, St_AccountException {
        Map<String,Object> object = new HashMap<>();
        St_MultiAuthToken auth = ((St_MultiAuthToken) authentication);
        St_Broker broker = brokerAccountService.getBrokerAccount(accountId, auth.getUserId());
        object.put("brokerAccount", new St_BrokerAccountDTO(broker));
        return object;
    }

    @GetMapping("/broker/get/all")
    public Map<String,Object> getUserBrokerAccount(Authentication authentication) throws IOException, St_AccountException {
        Map<String,Object> object = new HashMap<>();
        St_MultiAuthToken auth = ((St_MultiAuthToken) authentication);
        List<St_Broker> brokerList = brokerAccountService.getUserBrokerAccounts(auth.getUserId());
        object.put("accounts", brokerList.stream().map(i -> new St_BrokerAccountDTO(i)).collect(Collectors.toList()));
        return object;
    }
}
