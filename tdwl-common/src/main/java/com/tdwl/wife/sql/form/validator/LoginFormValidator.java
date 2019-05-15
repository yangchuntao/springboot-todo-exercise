package com.tdwl.wife.sql.form.validator;

import com.tdwl.wife.sql.form.LoginForm;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Yang Ct
 * @Description
 * @date
 **/
@Component
public class LoginFormValidator implements ConstraintValidator<LoginFormValid, LoginForm> {
    @Override
    public boolean isValid(LoginForm loginForm, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
