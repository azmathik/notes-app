package com.teletronics.notes.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Optional;

public class NotEmptyIfStringValidator implements ConstraintValidator<NotEmptyIfString, Object> {
    private NotEmptyIfString constraintAnnotation;

    @Override
    public void initialize(NotEmptyIfString constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {

        try {

            Field field = object.getClass().getDeclaredField(constraintAnnotation.targetField());
            field.setAccessible(true);
            String targetField = Optional.ofNullable(field.get(object)).orElse("").toString();

            Field dependent = object.getClass().getDeclaredField(constraintAnnotation.dependentField());
            dependent.setAccessible(true);
            String dependentField = Optional.ofNullable(dependent.get(object)).orElse("").toString();

            boolean isMatchCondition = switch (constraintAnnotation.condition()) {
                case EMPTY -> !StringUtils.hasLength(dependentField);
                case NOT_EMPTY -> StringUtils.hasLength(dependentField);
            };

            return !isMatchCondition || StringUtils.hasLength(targetField);
        } catch (Exception e) {
            throw new ValidationException(e);
        }
    }
}
