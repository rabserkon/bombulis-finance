package com.bombulis.accounting.service.AccountService.AccountProcessors;

import com.bombulis.accounting.service.AccountService.exception.Acc_AccountOtherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Acc_AccountProcessorsFactory {

    private Map<String, Acc_AccountProcessor> accountProcessors;

    @Autowired
    public Acc_AccountProcessorsFactory(List<Acc_AccountProcessor> accountProcessors) {
        this.accountProcessors = accountProcessors.stream().collect(Collectors.toMap(i -> i.getAccountType().name(), i -> i));
    }

    public Acc_AccountProcessor getProcessor(String type) throws Acc_AccountOtherType {
        Acc_AccountProcessor processor = accountProcessors.get(type);
        if (processor == null) {
            throw new Acc_AccountOtherType("Unknown account type: " + type);
        }
        return processor;
    }
}
