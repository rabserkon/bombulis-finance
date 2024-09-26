package com.bombulis.accounting.service.TransactionService.TransactionProcessors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TransactionProcessorFactory {
    private final Map<String, TransactionProcessor> processors;

    @Autowired
    public TransactionProcessorFactory(List<TransactionProcessor> processorList) {
        processors = processorList.stream()
                .collect(Collectors.toMap(p -> p.getType().name(), p -> p));
    }

    public TransactionProcessor getProcessor(String type) {
        TransactionProcessor processor = processors.get(type);
        if (processor == null) {
            throw new IllegalArgumentException("Unknown transaction type: " + type);
        }
        return processor;
    }
}
