package com.tdwl.wife.sql.po;

import com.tdwl.wife.sql.po.enums.Role;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author Yang Ct
 * @Description 用于用户登陆
 * @date
 **/
@Data
@ToString
public class User extends AbstractEntity{

    //id使用雪花算法，将原子性和纳秒加进去

    //用户名
    private String name;

    //密码
    private String password;

    //注册手机号
    private String mobile;

    //创建时间
    private Date createDate;

    //更新时间
    private Date updateDate;

    //用户角色
    private Role role;

    //用户登陆ip
    private String loginIp;

    //用户电脑用户名
    private String userName;

    //上次登陆时间
    private Date lastLoginTime;

}
