package com.tdwl.wife.sql.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class ClientIPHelper {

    /**
     * 
     * getClientIp:nginx proxy set IP
     *
     * @param @param request
     * @param @return 设定文件
     * @throws String
     *             DOM对象
     * @since JDK 1.8
     */
    public static String getClientIp(HttpServletRequest request) {
        // nginx proxy_set_header
        String[] res = { "X-real-ip", "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "REMOTE-HOST" };
        for (String fromSource : res) {
            String ip = request.getHeader(fromSource);
            log.debug(fromSource + " = {}", ip);
            if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return "";
    }
}
