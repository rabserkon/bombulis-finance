package com.bombulis.accounting.service.TransactionService.BalanceProcessors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BalanceProcessorFactory {
    private final Map<String, BalanceProcessor> processors;

    @Autowired
    public BalanceProcessorFactory(List<BalanceProcessor> processorList) {
        processors = processorList.stream()
                .collect(Collectors.toMap(p -> p.getType().name(), p -> p));
    }

    public BalanceProcessor getProcessor(String type) {
        BalanceProcessor processor = processors.get(type);
        if (processor == null) {
            throw new IllegalArgumentException("Unknown transaction type: " + type);
        }
        return processor;
    }
}
