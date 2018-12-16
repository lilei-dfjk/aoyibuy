package com.aoyibuy.commons.utils;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 日期处理工具类
 * 
 * @author wh
 * @since 0.0.1 
 */
public abstract class DateUtils {

    public static String formatNow(String pattern) {
        return format(new Date(), pattern);
    }
    
    public static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }
    
    public static Date parse(String date, String pattern) {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(date, pattern);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String today() {
        return formatNow("yyyyMMdd");
    }
    
}
