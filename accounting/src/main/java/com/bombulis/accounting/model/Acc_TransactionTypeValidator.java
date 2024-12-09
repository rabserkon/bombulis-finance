package com.bombulis.accounting.model;

import com.bombulis.accounting.service.TransactionService.TransactionProcessors.Acc_TransactionType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Acc_TransactionTypeValidator implements ConstraintValidator<Acc_ValidTransactionType, String> {
    @Override
    public void initialize(Acc_ValidTransactionType constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        for (Acc_TransactionType type : Acc_TransactionType.values()) {
            if (type.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
