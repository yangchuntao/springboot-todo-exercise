package com.tdwl.wife.sql.exception;

/**
 * ClassName:ReturnCode Function: Date: 2015年11月3日 下午12:17:04
 *
 * @version 1.0
 * @since JDK 1.8
 * @see
 */
public enum ExceptionMsg {

    SUCCESS("10000", "成功"),REQUEST_SUCCESS_NODATA_IN_ABOUT_TABLE("10010", "请求虽然成功,但是数据库没有数据,about数据库必须至少有一条记录"),

    SESSION_TIME_OUT("10001", "会话超时了，请重新登录"), SESSION_INVALID("10002", "会话无效，请重新登录"), TOKEN_NOT_FOUND("10003", "会话令牌不能为空"),

    PARAM_INVALID("10004", "请求参数无效"), UN_ACCESSABLE("10005", "没有权限"), REQUEST_INVALID("10006", "请求参数不合法"), SIGN_INVALID("10007", "签名规则不合法"), REQUEST_URL_INVALID(
            "10008", "匹配不到合适的URL，可能是GET/POST请求方式或者URL路径不正确"), PASSWORD_INVALID("10009", "密码错误"),

    ARITHMETIC_EXCEPTION("20001", "数学运算异常！"), SQL_EXCEPTION("20002", "操作数据库异常！"), SECURITY_EXCEPTION("20003", "违背安全原则异常！"), USERNAME_NOT_FOUND(
            "20004", "用户名/手机号不存在"), VCODE_INVALID("20005", "短信验证码不正确"), USERNAME_ALREADY_EXISTS("20006", "用户名/手机号已经被注册了"), NO_SUCH_ELEMENT(
            "20007", "找不到对象"), ELEMENT_ALREADY_EXISTS("20008", "对象已经存在了"), RECORD_ALREADY_EXISTS("20018", "存在重复的值，操作失败"),VCODE_SEND_FAIL("20019", "验证码发送失败"),
    MESSAGE_SEND_FAIL("20020", "信息发送失败"),MOBILE_NULL("9999","手机号码为空"),MOBILE_INVALID("9998","手机号码格式不正确"),

    TODO_FUNCTION_EXCEPTION("99987", "不可使用未完成的功能方法"), NULL_VALUE_EXCEPTION("99988", "参数不能为空值"), IO_EXCEPTION("99989", "IO异常！"), JVM_INTERNAL_ERROR(
            "99990", "Java虚拟机发生了内部错误"), CONNECT_TIMEOUT("99991", "连接到数据库异常"), METHOD_NOT_FOUND("99992", "方法末找到异常！"), CLASS_CAST_EXCEPTION(
            "99993", "类型强制转换错误！"), ILLEGAL_ARGUMENT_EXCEPTION("99994", "方法的参数错误！"), ARRAY_INDEX_OUTOFBOUND("99995", "数组下标越界!"), NO_CURRENT_USER_PARAMER_IN_CONTROLLER_FUNCTION(
            "99995", "controller类的方法如果加上了RequireSession注解，需要添加CurrentUser参数"), NULL_POINT_EXCEPTION("99996", "发生了空指针异常"), CLASS_NOT_FOUND(
            "99997", "程序某个类找不到"), BUSINESS_ERROR("99998", "业务逻辑异常"), DEFAULT_ERROR("99999", "未知的程序错误"),USERNAME_EXISTS("20009","用户名已经存在"),
        PASSWORD_DIFFERENT("20010","两次密码输入不一致！"),FILE_CONTENT_EMPTY("20011","没有传入照片！"),USENAME_INVALID("20112","用户名无效"),

    FORM_SUBMIT_ERROR("30001", "表单提交错误！可能是由于您连续提交了同一表单（例如，双击了提交按钮）或访问了过期表单页面。"), PUSH_EXCEPTION("20021", "推送消息发生错误了"),
    FROM_SUBMIT_FAIL("20022", "表单提交失败！插入数据失败"),
    EXCEED_THE_MAXIMUM("20023","超过了最大值");

    private String code;
    private String msg;

    ExceptionMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
