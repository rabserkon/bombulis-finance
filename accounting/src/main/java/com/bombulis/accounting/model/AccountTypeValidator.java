package com.bombulis.accounting.model;

import com.bombulis.accounting.service.AccountService.AccountType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AccountTypeValidator implements ConstraintValidator<ValidAccountType, String> {
    @Override
    public void initialize(ValidAccountType constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        for (AccountType type : AccountType.values()) {
            if (type.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}