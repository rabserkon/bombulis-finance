package com.bombulis.accounting.service.RevaluationService.RevaluationProcessors;

import com.bombulis.accounting.service.AccountService.exception.Acc_AccountOtherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Acc_RevaluationProcessorFactory {

    private Map<String, Acc_RevaluationProcessor> revaluationProcessors;

    @Autowired
    public Acc_RevaluationProcessorFactory(List<Acc_RevaluationProcessor> revaluationProcessors) {
        this.revaluationProcessors =
                revaluationProcessors.stream().collect(Collectors.toMap(i -> i.getType(), i->i));
    }

    public Acc_RevaluationProcessor getProcessor(String type) throws Acc_AccountOtherType {
        Acc_RevaluationProcessor processor = this.revaluationProcessors.get(type);
        if (processor == null) {
            throw new Acc_AccountOtherType("Unknown transaction type: " + type);
        }
        return processor;
    }
}
