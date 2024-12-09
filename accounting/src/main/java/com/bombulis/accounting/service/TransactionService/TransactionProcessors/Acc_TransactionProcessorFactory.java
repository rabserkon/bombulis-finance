package com.bombulis.accounting.service.TransactionService.TransactionProcessors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Acc_TransactionProcessorFactory {
    private final Map<String, Acc_TransactionProcessor> processors;

    @Autowired
    public Acc_TransactionProcessorFactory(List<Acc_TransactionProcessor> processorList) {
        processors = processorList.stream()
                .collect(Collectors.toMap(p -> p.getType().name(), p -> p));
    }

    public Acc_TransactionProcessor getProcessor(String type) {
        Acc_TransactionProcessor processor = processors.get(type);
        if (processor == null) {
            throw new IllegalArgumentException("Unknown transaction type: " + type);
        }
        return processor;
    }
}
