package com.tdwl.wife.sql.po.enums;

import lombok.Getter;

/**
 * @author Yang Ct
 * @Description
 * @date
 **/
@Getter
public enum ShortMessageType {

    VALID_CODE("注册登陆验证码"),
    RESET_PASSWORD("重置密码验证码");
    private String name;

    ShortMessageType(String name){
        this.name = name;
    }
}
