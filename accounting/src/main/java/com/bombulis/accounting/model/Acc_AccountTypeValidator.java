package com.bombulis.accounting.model;

import com.bombulis.accounting.service.AccountService.Acc_AccountType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Acc_AccountTypeValidator implements ConstraintValidator<Acc_ValidAccountType, String> {
    @Override
    public void initialize(Acc_ValidAccountType constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        for (Acc_AccountType type : Acc_AccountType.values()) {
            if (type.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}