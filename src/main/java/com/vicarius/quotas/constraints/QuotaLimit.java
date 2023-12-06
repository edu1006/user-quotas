package com.vicarius.quotas.constraints;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QuotaLimit {
    String message() default "User quota exceeded";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
