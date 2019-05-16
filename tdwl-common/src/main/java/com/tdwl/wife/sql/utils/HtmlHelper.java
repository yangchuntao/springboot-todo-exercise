package com.tdwl.wife.sql.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName HtmlHelper
 * @Description HtmlHelper
 * @Author HanKeQi
 * @Date 2018/7/30 上午11:24
 * @Version 1.0.0
 **/
public class HtmlHelper {


    //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
    private static final String REGEX_SCRIPT = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
    //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
    private static final String REGEX_STYLE = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
    // 定义HTML标签的正则表达式
    private static final String REGEX_HTML = "<[^>]+>";
    // 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    private static final String REGEX_SPECIAL = "\\&[a-zA-Z]{1,10};";

    // 含html标签的字符串
    public static String removeHtmlTag(String htmlStr) {
        String textStr = "";
        if (StringUtils.isNotEmpty(htmlStr)) {
            try {
                Pattern pScript = Pattern.compile(REGEX_SCRIPT, Pattern.CASE_INSENSITIVE);
                Matcher mScript = pScript.matcher(htmlStr);
                htmlStr = mScript.replaceAll(""); // 过滤script标签
                Pattern pStyle = Pattern.compile(REGEX_STYLE, Pattern.CASE_INSENSITIVE);
                Matcher mStyle = pStyle.matcher(htmlStr);
                htmlStr = mStyle.replaceAll(""); // 过滤style标签
                Pattern pHtml = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
                Matcher mHtml = pHtml.matcher(htmlStr);
                htmlStr = mHtml.replaceAll(""); // 过滤html标签
                Pattern pSpecial = Pattern.compile(REGEX_SPECIAL, Pattern.CASE_INSENSITIVE);
                Matcher mSpecial = pSpecial.matcher(htmlStr);
                htmlStr = mSpecial.replaceAll(""); // 过滤特殊标签
                textStr = htmlStr;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return textStr;// 返回文本字符串

    }

}