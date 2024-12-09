package com.bombulis.stock.control.component.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class St_OperationTypeValidator implements ConstraintValidator<St_OperationValidType, String> {
    @Override
    public void initialize(St_OperationValidType constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && (value.equalsIgnoreCase("buy") || value.equalsIgnoreCase("sell"));
    }
}
