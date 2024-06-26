package com.bombulis.accounting.model;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TransactionTypeValidator.class)
public @interface ValidTransactionType {
    String message() default "Invalid account type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}