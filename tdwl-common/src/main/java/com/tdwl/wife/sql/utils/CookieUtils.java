package com.tdwl.wife.sql.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    /**
     * 设置cookie
     * 
     * @param response
     * @param cookieName
     * @param value
     * @param maxAge
     */
    public static void addCookie(HttpServletResponse response, HttpServletRequest request, String cookieName, String value, int maxAge) {
        String path = request.getContextPath();
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setPath(path);
        if (StringUtils.isEmpty(path)) {
            cookie.setPath("/");
        }
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 设置cookie 默认关闭浏览器失效
     * 
     * @param response
     * @param cookieName
     * @param value
     */
    public static void addCookie(HttpServletResponse response, HttpServletRequest request, String cookieName, String value) {
        addCookie(response, request, cookieName, value, -1);
    }

    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie cookie = getCookie(request, cookieName);
        if (null != cookie) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    /**
     * 获取cookie的值
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie cookie = getCookie(request, cookieName);
        if (cookie == null) {
            return null;
        }
        return cookie.getValue();
    }

    /**
     * 返回Cookie
     * 
     * @param request
     * @param cookieName
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie ck[] = request.getCookies();
        if (ck == null)
            return null;
        for (Cookie c : ck) {
            if (c.getName().equals(cookieName)) {
                return c;
            }
        }
        return null;
    }

}
