package com.tdwl.wife.sql.form.validator;

import com.tdwl.wife.sql.form.LoginForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${mobileNum.regex}")
    private String mobileRegex;

    @Value("${}")
    private String nameRegex;

    @Value("${}")
    private String passwordRegex;

    @Value("${}")
    private String validCodeRegex;




    @Override
    public boolean isValid(LoginForm form, ConstraintValidatorContext constraintValidatorContext) {
        log.info("name={}, password={}, mobile={}", form.getName(), form.getPassword(), form.getMobile());
        //对传入的4个参数都需要校验，设置一定的规则
        if (!StringUtils.isBlank(form.getName()) || form.getName().matches(nameRegex)){
            constraintValidatorContext.buildConstraintViolationWithTemplate("用户名必须同时包含大写字母，小写字母，数字,且长度不小于6位").addPropertyNode("name").addConstraintViolation();
            return false;
        }
        if (!StringUtils.isBlank(form.getMobile()) || form.getMobile().matches(mobileRegex)){
            constraintValidatorContext.buildConstraintViolationWithTemplate("手机号码格式不正确").addPropertyNode("mobile").addConstraintViolation();
            return false;
        }
        if (!StringUtils.isBlank(form.getPassword()) || form.getPassword().matches(passwordRegex)){
            constraintValidatorContext.buildConstraintViolationWithTemplate("密码必须同时包含大写字母，小写字母，数字,且长度不小于6位").addPropertyNode("password").addConstraintViolation();
            return false;
        }
        if (!StringUtils.isBlank(form.getValidCode()) || form.getValidCode().matches(validCodeRegex)){
            constraintValidatorContext.buildConstraintViolationWithTemplate("验证码长度是6位").addPropertyNode("validCode").addConstraintViolation();
        }
        //context.buildConstraintViolationWithTemplate("手机号码已被注册！").addPropertyNode("cell").addConstraintViolation();

        return true;
    }
}
