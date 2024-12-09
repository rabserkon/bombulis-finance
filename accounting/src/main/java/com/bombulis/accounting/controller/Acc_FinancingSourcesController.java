package com.bombulis.accounting.controller;

import com.bombulis.accounting.component.Acc_MultiAuthToken;
import com.bombulis.accounting.dto.Acc_AccountDTO;
import com.bombulis.accounting.dto.Acc_ResponseAccountDTO;
import com.bombulis.accounting.dto.Acc_SourceDTO;
import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.service.AccountService.Acc_AccountService;
import com.bombulis.accounting.service.AccountService.Acc_FinancingSourcesService;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountException;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyNonFound;
import com.bombulis.accounting.service.UserService.Acc_UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class Acc_FinancingSourcesController {

    @Autowired
    private Acc_AccountService accountService;
    @Autowired
    private Acc_FinancingSourcesService sourcesService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAccount(@Valid @RequestBody Acc_SourceDTO accountDTO,
                                           Authentication authentication) throws Acc_CurrencyNonFound, Acc_UserException, Acc_AccountException {
        Acc_Account account = accountService.createAccount(new Acc_AccountDTO(accountDTO), ((Acc_MultiAuthToken) authentication).getUserId());
        return ResponseEntity.ok(new Acc_ResponseAccountDTO(account));
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listAccount(Authentication authentication) throws Acc_CurrencyNonFound, Acc_UserException, Acc_AccountException {
        Map<String, Object> response = new HashMap<>();
        Acc_MultiAuthToken authToken = ((Acc_MultiAuthToken) authentication);
        response.put("source_list",sourcesService.getListDepositAccount(authToken.getUserId())
                .stream().map(i -> new Acc_ResponseAccountDTO(i))
                .collect(Collectors.toList()));
        response.put("withdrawal_list", sourcesService.getListWithdrawalAccount(authToken.getUserId())
                .stream().map(i -> new Acc_ResponseAccountDTO(i))
                .collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }
}
