package com.bombulis.accounting.controller;

import com.bombulis.accounting.component.MultiAuthToken;
import com.bombulis.accounting.dto.AccountDTO;
import com.bombulis.accounting.dto.ResponseAccountDTO;
import com.bombulis.accounting.dto.SourceDTO;
import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.service.AccountService.AccountService;
import com.bombulis.accounting.service.AccountService.FinancingSourcesService;
import com.bombulis.accounting.service.AccountService.exception.AccountException;
import com.bombulis.accounting.service.CurrencyService.CurrencyNonFound;
import com.bombulis.accounting.service.UserService.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/sources")
public class FinancingSourcesController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private FinancingSourcesService sourcesService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAccount(@Valid @RequestBody SourceDTO accountDTO,
                                           Authentication authentication) throws CurrencyNonFound, UserException, AccountException {
        Account account = accountService.createAccount(new AccountDTO(accountDTO), ((MultiAuthToken) authentication).getUserId());
        return ResponseEntity.ok(new ResponseAccountDTO(account));
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listAccount(Authentication authentication) throws CurrencyNonFound, UserException, AccountException {
        Map<String, Object> response = new HashMap<>();
        MultiAuthToken authToken = ((MultiAuthToken) authentication);
        response.put("source_list",sourcesService.getListDepositAccount(authToken.getUserId())
                .stream().map(i -> new ResponseAccountDTO(i))
                .collect(Collectors.toList()));
        response.put("withdrawal_list", sourcesService.getListWithdrawalAccount(authToken.getUserId())
                .stream().map(i -> new ResponseAccountDTO(i))
                .collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }
}
