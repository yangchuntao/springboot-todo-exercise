package com.tdwl.wife.sql.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 所有时间相关计算请使用 JDK 8 java.time包(joda time project). 该类只提供 JDK 8 java.time 包与 java.util.Date 的相互转换.
 */
public final class DateTimeHelper {

    public static final String SHORT_DATE = "yyyy-MM-dd";

    public static final String DEF_DATE = "yyyy-MM-dd HH:mm:ss";

    public static final String NOR_DATE = "yyyy-MM-dd HH:mm";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    public static final String MM_DD_HH_MM = "MM/dd HH:mm";

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return dateToLocalDateTime(date, ZoneId.systemDefault());
    }

    public static LocalDateTime dateToLocalDateTime(Date date, ZoneId tzId) {
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, tzId);
    }

    public static LocalDate dateToLocalDate(Date date) {
        return dateToLocalDate(date, ZoneId.systemDefault());
    }

    public static LocalDate dateToLocalDate(Date date, ZoneId tzId) {
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, tzId).toLocalDate();
    }

    public static LocalTime dateToLocalTime(Date date) {
        return dateToLocalTime(date, ZoneId.systemDefault());
    }

    public static LocalTime dateToLocalTime(Date date, ZoneId tzId) {
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, tzId).toLocalTime();
    }

    public static Date localDateTimeToDate(LocalDateTime ldt) {
        return localDateTimeToDate(ldt, ZoneId.systemDefault());
    }

    public static Date localDateTimeToDate(LocalDateTime ldt, ZoneId tzId) {
        Instant instant = ldt.atZone(tzId).toInstant();
        return Date.from(instant);
    }

    public static Date localDateToDate(LocalDate ld) {
        return localDateToDate(ld, ZoneId.systemDefault());
    }

    public static Date localDateToDate(LocalDate ld, ZoneId tzId) {
        Instant instant = ld.atStartOfDay().atZone(tzId).toInstant();
        return Date.from(instant);
    }

    public static Date localTimeToDate(LocalTime lt) {
        return localTimeToDate(lt, ZoneId.systemDefault());
    }

    public static Date localTimeToDate(LocalTime lt, ZoneId tzId) {
        Instant instant = lt.atDate(LocalDate.now()).atZone(tzId).toInstant();
        return Date.from(instant);
    }

    public static Date localTimeToDate(LocalTime lt, int year, int month, int day) {
        return localTimeToDate(lt, ZoneId.systemDefault(), year, month, day);
    }

    public static Date localTimeToDate(LocalTime lt, ZoneId tzId, int year, int month, int day) {
        Instant instant = lt.atDate(LocalDate.of(year, month, day)).atZone(tzId).toInstant();
        return Date.from(instant);
    }

    public static String getDateToFormatter(Date date, String Formatter) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern(Formatter);
        LocalDateTime dateTime = dateToLocalDateTime(date);
        return dateTime.format(f);
    }

    public static Date getStrToDate(String date) {
        LocalDateTime parse = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DEF_DATE));
        return localDateTimeToDate(parse);
    }

    public static LocalDate getStrToLocalDate(String date, String pattern) {
        LocalDate parse = LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
        return parse;
    }

    //当天呢的小时相加，大于24小时候，还是当前
    public static Date getSpecificLocalTimeDaysHoursMinutes(Date date, Integer hours, Integer minutes) {
        LocalTime localTime = dateToLocalTime(date);
        if (hours  != null )
            localTime = localTime.plusHours(hours);
        if (minutes != null){
            localTime = localTime.plusMinutes(minutes);
        }
        return localTimeToDate(localTime);
    }

    public static Date getSpecificLocalDateTimeDaysHoursMinutes(Date date, Integer days, Integer hours, Integer minutes) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        if (days != null){
            localDateTime = localDateTime.plusDays(days);
        }
        if (hours  != null )
            localDateTime = localDateTime.plusHours(hours);
        if (minutes != null){
            localDateTime = localDateTime.plusMinutes(minutes);
        }
        return localDateTimeToDate(localDateTime);
    }

    public static String dateToString(Date date, String formatter) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern(formatter);
        LocalDateTime dateTime = dateToLocalDateTime(date);
        return dateTime.format(f);
    }

    public static LocalDateTime longToLocalTime(Long date){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date),ZoneId.of("Asia/Shanghai"));
    }

    public static String longToString(Long date, String formatter){
        DateTimeFormatter f = DateTimeFormatter.ofPattern(formatter);
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.of("Asia/Shanghai"));
        return dateTime.format(f);
    }

    public static String dateDetails(Date date){
        Date currTime = new Date();
        String str = "1分钟前";
        int year = currTime.getYear() - date.getYear();//年
        int month = currTime.getMonth() - date.getMonth();
        int day = currTime.getDate() - date.getDate();
        int hour = currTime.getHours() - date.getHours();
        int min = currTime.getMinutes() - date.getMinutes();
        if (year>=1){
            str = String.format("%s年前", year);
        } else if (month >= 1){
            str = String.format("%s月前", month);
        } else if (day>=1){
            str = String.format("%s天前", day);
        } else if (hour>=1){
            str = String.format("%s小时前", hour);
        } else if (min>=1){
            str = String.format("%s分钟前", min);
        }
        return str;
    }

}
