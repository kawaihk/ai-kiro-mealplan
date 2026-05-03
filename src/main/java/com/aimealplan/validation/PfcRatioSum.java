package com.aimealplan.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PfcRatioSumValidator.class)
@Documented
public @interface PfcRatioSum {
    String message() default "PFC比率の合計は100%である必要があります";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}