
package com.tdwl.wife.sql.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;


public class UrlParamUtils {

    /**
     * 将url参数转换成map，取值按照插入顺序
     * 
     * @param param
     *            aa=11&bb=22&cc=33
     * @return
     */
    public static Map<String, String> getUrlParamsOnsequence(String param) {
        Map<String, String> map = new LinkedHashMap<String, String>(0);
        if (org.apache.commons.lang3.StringUtils.isBlank(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            int index = params[i].indexOf('=');
            if (index == -1) {
                map.put(params[i], "");
            } else {
                map.put(params[i].substring(0, index), params[i].substring(index + 1));
            }
            // 废弃，不适用于参数值有等号的情况
            // String[] p = params[i].split("=");
            // if (p.length == 2) {
            // map.put(p[0], p[1]);
            // }
        }
        return map;
    }

    /**
     * 
     * getQueryParamValue:取url的参数
     *
     * @param key
     * @param url
     * @return 设定文件
     * @throws List
     *             <String> DOM对象
     * @since JDK 1.8
     */
    public static List<String> getQueryParamValue(String key, String url) {
        return getQueryParams(url).get(key);
    }

    /**
     * 
     * getQueryParams: 将url参数转换成map，取值顺序不可预知
     *
     * @param url
     *            http://www.abc.com?a=12&b=23&c=hello
     * @return map，取值顺序不可预知
     * @since JDK 1.8
     */
    public static Map<String, List<String>> getQueryParams(String url) {
        try {
            Map<String, List<String>> params = new HashMap<String, List<String>>();
            String[] urlParts = url.split("\\?");
            if (urlParts.length > 1) {
                String query = urlParts[1];
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if (pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }
                    List<String> values = params.get(key);
                    if (values == null) {
                        values = new ArrayList<String>();
                        params.put(key, values);
                    }
                    values.add(value);
                }
            }
            // log.debug(String.format("Parsed params: %s", params));
            return params;
        } catch (UnsupportedEncodingException ex) {
            // log.error("Cannot parse params!", ex);
            throw new AssertionError(ex);
        }
    }

    /**
     * 将map转换成url参数形式
     * 
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = org.apache.commons.lang3.StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }
}
