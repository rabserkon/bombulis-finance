package com.bombulis.accounting.service.AccountService.AccountProcessors;

import com.bombulis.accounting.service.AccountService.exception.AccountOtherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AccountProcessorsFactory {

    private Map<String, AccountProcessor> accountProcessors;

    @Autowired
    public AccountProcessorsFactory(List<AccountProcessor> accountProcessors) {
        this.accountProcessors = accountProcessors.stream().collect(Collectors.toMap(i -> i.getAccountType().name(), i -> i));
    }

    public AccountProcessor getProcessor(String type) throws AccountOtherType {
        AccountProcessor processor = accountProcessors.get(type);
        if (processor == null) {
            throw new AccountOtherType("Unknown account type: " + type);
        }
        return processor;
    }
}
