package com.tdwl.wife.sql.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientTypeHelper {

    // \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),
    // 字符串在编译时会被转码一次,所以是 "\\b"
    // \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
    private static String phoneReg = "\\bNokia|SAMSUNG|MIDP-2|CLDC1.1|SymbianOS|MAUI|UNTRUSTED/1.0"
            + "|Windows CE|iPhone|iPad|Android|BlackBerry|UCWEB|ucweb|BREW|J2ME|YULONG|YuLong|COOLPAD|TIANYU|TY-"
            + "|K-Touch|Haier|DOPOD|Lenovo|LENOVO|HUAQIN|AIGO-|CTC/1.0"
            + "|CTC/2.0|CMCC|DAXIAN|MOT-|SonyEricsson|GIONEE|HTC|ZTE|HUAWEI|webOS|GoBrowser|IEMobile|WAP2.0\\b";
    private static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser" + "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";

    // 移动设备正则匹配：手机端、平板
    private static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
    private static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

    /**
     * 检测是否是移动设备访问
     * 
     */
    public static boolean isMobileBroswer(String userAgent) {
        if (null == userAgent) {
            userAgent = "";
        }
        Matcher matcherPhone = phonePat.matcher(userAgent);
        Matcher matcherTable = tablePat.matcher(userAgent);
        if (matcherPhone.find() || matcherTable.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测是否是微信
     * 
     */
    public static boolean isWeixinBroswer(String userAgent) {
        if (null == userAgent) {
            userAgent = "";// "Mozilla/5.0(iphone;CPU iphone OS 5_1_1 like Mac OS X) AppleWebKit/534.46(KHTML,like Geocko) Mobile/9B206 MicroMessenger/5.0";
        }
        userAgent = userAgent.toLowerCase();
        int micIndex = userAgent.indexOf("micromessenger");
        if (micIndex > -1) {
            String version = userAgent.substring(micIndex + 15, micIndex + 18);
            try {
                if (Double.parseDouble(version) < 5) {// 微信5.0版本后才加入微信支付模块
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

}
