package com.tdwl.wife.sql.po.enums;

/**
 * @author Yang Ct
 * @Description 用户类型的枚举类
 * @date
 **/
public enum Role {

    ADMIN("管理员"),
    WIFE("媳妇"),
    USER("普通用户");

    private String name;

    public String getName(){
        return name;
    }

    Role(String name){
        this.name = name;
    }
}
