package com.tdwl.wife.sql.form.validator;

import com.tdwl.wife.sql.form.LoginForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Yang Ct
 * @Description
 * @date
 **/
@Component
@Slf4j
public class LoginFormValidator implements ConstraintValidator<LoginFormValid, LoginForm> {
    @Override
    public boolean isValid(LoginForm form, ConstraintValidatorContext constraintValidatorContext) {
        log.info("validator={}",form.getName());
        System.out.println(form.getMobile());
        //context.buildConstraintViolationWithTemplate("手机号码已被注册！").addPropertyNode("cell").addConstraintViolation();

        return false;
    }
}
