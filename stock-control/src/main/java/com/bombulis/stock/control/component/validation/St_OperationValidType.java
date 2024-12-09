package com.bombulis.stock.control.component.validation;

import com.nimbusds.jose.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = St_OperationTypeValidator.class) // Укажите валидатор
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface St_OperationValidType {
    String message() default "Invalid type. Must be 'buy' or 'sell'."; // Сообщение об ошибке
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
