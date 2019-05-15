package com.tdwl.wife.sql.form.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;

/**
 * @author Yang Ct
 * @Description 登陆表单验证
 * @date
 **/
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginFormValidator.class)
@Documented
public @interface LoginFormValid {
    // default error message
    String message() default "已被注册！";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
