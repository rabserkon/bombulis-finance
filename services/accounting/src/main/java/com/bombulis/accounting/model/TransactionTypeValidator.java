package com.bombulis.accounting.model;

import com.bombulis.accounting.service.TransactionService.TransactionProcessors.TransactionType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TransactionTypeValidator implements ConstraintValidator<ValidTransactionType, String> {
    @Override
    public void initialize(ValidTransactionType constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        for (TransactionType type : TransactionType.values()) {
            if (type.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
