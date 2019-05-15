package com.tdwl.wife.sql.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tdwl.wife.sql.exception.ExceptionMsg;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    
    
    @Value("${mobileNum.regex}")
    private String mobileRegex;

    @Value("${shortMessage.url}")
    private String url;
    
    
    @Autowired
    RedisTemplate<String, String> redisTemplate;


    public  static final String SMS_PREFIX = "【杨春涛告诉你】您的验证码是";

    public static final String SMS_SUFFIX="。如非本人操作，请忽略本短信";
    /**
     * 1.用户登陆接口
     *
     */


    /**
     * 2.获取验证码，需要将用户获取的验证码和验证码的次数存到redis中
     *
     * //获取短信验证码接口：https://sms.yunpian.com/v2/sms/single_send.json
     */
    @RequestMapping(value = "/getValidCode", method = RequestMethod.GET)
    public ResultMap getValidCode(String mobile){
        log.info("mobile = {}", mobile);
        //首先该号码是否是电话号码，如果不是请作判断
        ResultMap resultMap = new ResultMap();
        if (StringUtils.isEmpty(mobile)){
            return ResultMap.newInstance(ExceptionMsg.MOBILE_NULL);
        }
        if (!mobile.matches(mobileRegex)){
            return ResultMap.newInstance(ExceptionMsg.MOBILE_INVALID);
        }
        //如果符合要求，开始走验证码
        //1.随机生成6位数验证码
        String randomNums = RandomUtils.getRandomNums(6);
        //2.将生成的数放到redis中,并且设置过期时间，后期还要限制每人发送的条数
        try {
            String key = mobile + ":" + String.valueOf(ShortMessageType.VALID_CODE);
            redisTemplate.opsForValue().set(key, randomNums, 5, TimeUnit.MINUTES);
            log.info("validcode 插入到数据库");
            //组装信息后开始调用接口
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
        redisTemplate.opsForValue().set("name",name);
        return name;
    }

    /**
     * 3.设置密码，用户进入界面后需要设置密码
     */


}
