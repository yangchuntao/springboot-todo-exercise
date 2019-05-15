package com.tdwl.wife.sql.form;

import com.tdwl.wife.sql.form.validator.LoginFormValid;
import lombok.Data;

/**
 * @author Yang Ct
 * @Description 登陆表单
 * @date
 **/
@Data
@LoginFormValid
public class LoginForm extends AbstractForm{

    private String name;

    private String mobile;

    private String validCode;

    private String password;

}
