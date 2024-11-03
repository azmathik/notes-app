package com.teletronics.notes.validators;

import jakarta.validation.*;
import org.springframework.util.StringUtils;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.Optional;



@Constraint(validatedBy = NotEmptyIfString.NotEmptyIfStringValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(NotEmptyIfString.List.class)
public @interface NotEmptyIfString {

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "{targetField} not passed the {condition} validation based on {dependentField}";

    String targetField();

    String dependentField();

    Condition condition();

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        NotEmptyIfString[] value();
    }

    class NotEmptyIfStringValidator implements ConstraintValidator<NotEmptyIfString, Object> {

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

    enum Condition {
        EMPTY, NOT_EMPTY
    }

}
