package com.bombulis.accounting.service.RevaluationService.RevaluationProcessors;

import com.bombulis.accounting.service.AccountService.exception.AccountOtherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RevaluationProcessorFactory {

    private Map<String, RevaluationProcessor> revaluationProcessors;

    @Autowired
    public RevaluationProcessorFactory(List<RevaluationProcessor> revaluationProcessors) {
        this.revaluationProcessors =
                revaluationProcessors.stream().collect(Collectors.toMap(i -> i.getType(), i->i));
    }

    public RevaluationProcessor getProcessor(String type) throws AccountOtherType {
        RevaluationProcessor processor = this.revaluationProcessors.get(type);
        if (processor == null) {
            throw new AccountOtherType("Unknown transaction type: " + type);
        }
        return processor;
    }
}
