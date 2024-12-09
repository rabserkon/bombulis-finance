package com.bombulis.accounting.service.TransactionService.BalanceProcessors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Acc_BalanceProcessorFactory {
    private final Map<String, Acc_BalanceProcessor> processors;

    @Autowired
    public Acc_BalanceProcessorFactory(List<Acc_BalanceProcessor> processorList) {
        processors = processorList.stream()
                .collect(Collectors.toMap(p -> p.getType().name(), p -> p));
    }

    public Acc_BalanceProcessor getProcessor(String type) {
        Acc_BalanceProcessor processor = processors.get(type);
        if (processor == null) {
            throw new IllegalArgumentException("Unknown transaction type: " + type);
        }
        return processor;
    }
}
