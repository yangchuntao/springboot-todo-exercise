package com.tdwl.wife.sql.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tdwl.wife.sql.exception.ExceptionMsg;
import com.tdwl.wife.sql.form.LoginForm;
import com.tdwl.wife.sql.po.enums.ShortMessageType;
import com.tdwl.wife.sql.utils.HttpClientUtils;
import com.tdwl.wife.sql.utils.RandomUtils;
import com.tdwl.wife.sql.utils.ResultMap;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Yang Ct
 * @Description 用户登陆接口
 * @date
 **/
@RestController
@RequestMapping("/account")
@Slf4j
public class UserLoginController {


    @Value("${excesice.sql.secretKey}")
    private String secretKey;
    //TODO 将这个抽成一个注解
    @Value("${mobileNum.regex}")
    private String mobileRegex;

    @Value("${shortMessage.url}")
    private String url;

    @Autowired
    RedisTemplate<String, String> redisTemplate;


    public  static final String SMS_PREFIX = "【杨春涛告诉你】您的验证码是";

    public static final String SMS_SUFFIX="。如非本人操作，请忽略本短信";
    /**
     * 1.用户登陆接口,并且注册，用户需要填写loginForm中的内容，后续需要补全的资料可以在登陆后到个人中心进行修改
     *
     */
    @RequestMapping(value = {"/login","/login/"}, method = RequestMethod.POST)
    public ResultMap login(@Valid LoginForm form, BindingResult bindingResult){
        log.info("form={},{},{},{}",form.getMobile(),form.getName(),form.getPassword(),form.getValidCode());
        if (bindingResult.hasErrors()){
            return ResultMap.dataNewInstanceOfInvalidSimpleMsg(bindingResult);
        }


        return ResultMap.newInstance();
    }



    /**
     * 2.获取验证码，需要将用户获取的验证码和验证码的次数存到redis中
     *
     * //获取短信验证码接口：https://sms.yunpian.com/v2/sms/single_send.json
     */
    @RequestMapping(value = "/getValidCode", method = RequestMethod.GET)
    public ResultMap getValidCode(String mobile){
        log.info("mobile = {}", mobile);
        //TODO 以后将限制发送次数，针对手机号码
        ResultMap resultMap = new ResultMap();
        if (StringUtils.isEmpty(mobile)){
            return ResultMap.newInstance(ExceptionMsg.MOBILE_NULL);
        }
        if (!mobile.matches(mobileRegex)){
            return ResultMap.newInstance(ExceptionMsg.MOBILE_INVALID);
        }
        String randomNums = RandomUtils.getRandomNums(6);
        try {
            String key = mobile + ":" + String.valueOf(ShortMessageType.VALID_CODE);
            redisTemplate.opsForValue().set(key, randomNums, 5, TimeUnit.MINUTES);
            log.info("validcode 插入到数据库");
            String text = SMS_PREFIX + randomNums + SMS_SUFFIX;
            sendSms(url, mobile, text);
        }catch (Exception e){
            log.info("getValid:e = {}",e.getMessage());
            return ResultMap.newInstanceOfDefaultError("获取验证码失败！");
        }
        return ResultMap.newInstanceOfSuccess();
    }

    private void sendSms(String url, String mobile, String text) {
        log.info("url={},mobile={},text={}",url,mobile,text);
        HttpPost post = null;
        try {
            Map map = new HashMap();
            map.put("apikey",secretKey);
            map.put("mobile", mobile);
            map.put("text", text);
            String result = HttpClientUtils.postFormData(url,map);
        }catch (Exception e){
            log.info("e={}", e.getMessage());
        }
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(String name){
        //接受参数时要记得打日志
        log.info("name = {}", name);
        redisTemplate.opsForValue().set("name",name);
        return name;
    }

    /**
     * 3.设置密码，用户进入界面后需要设置密码
     */


}
