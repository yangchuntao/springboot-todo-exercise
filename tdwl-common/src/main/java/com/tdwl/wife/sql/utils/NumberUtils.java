package com.tdwl.wife.sql.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class NumberUtils {

    /**
     * objectToDecimal:金额对象转化为分
     * 
     */
    public static BigDecimal objectToDecimal(Object object, int n) {
        if (null != object) {
            try {
                String key = String.valueOf(object);
                BigDecimal b = new BigDecimal(key);
                b = b.divide(BigDecimal.ONE, n, BigDecimal.ROUND_HALF_UP);
                return b;
            } catch (Exception e) {
                return new BigDecimal(0);
            }
        }
        return new BigDecimal(0);
    }

    /**
     * yuanToFen:两位小数的金额 元转化为分 向上取整
     * 
     */
    public static String yuanToFen(Object object) {
        BigDecimal d = objectToDecimal(object, 2);
        if (null != d) {
            try {
                String valueOf = String.valueOf(d).replace(".", "");
                while (valueOf.startsWith("0")) {
                    valueOf = valueOf.substring(1, valueOf.length());
                }
                return valueOf;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * numberToPercentage:数字转换为百分之多少，保留两位小数，四舍五入， 如：0.00005121512转为0.01，0.92034转为92.03
     */
    public static String numberToPercentage(Object object) {
        BigDecimal decimal = objectToDecimal(object, 4);
        if (null != decimal) {
            try {
                String valueOf = String.valueOf(decimal).replace(".", "");
                while (valueOf.startsWith("0")) {
                    valueOf = valueOf.substring(1, valueOf.length());
                }
                if (StringUtils.isEmpty(valueOf)) {
                    return null;
                }
                if (valueOf.length() < 2) {
                    valueOf = String.format("%02d", Integer.parseInt(valueOf));
                }
                String prefix = valueOf.substring(0, valueOf.length() - 2);
                String suffix = valueOf.substring(valueOf.length() - 2, valueOf.length());
                while (suffix.endsWith("0")) {
                    suffix = suffix.substring(0, suffix.length() - 1);
                }
                if (StringUtils.isEmpty(suffix)) {
                    return prefix;
                }
                if (StringUtils.isEmpty(prefix)) {
                    return "0." + suffix;
                }
                return prefix + "." + suffix;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

}
