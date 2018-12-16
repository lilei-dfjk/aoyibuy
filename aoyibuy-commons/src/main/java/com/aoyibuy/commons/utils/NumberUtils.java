package com.aoyibuy.commons.utils;

import java.text.DecimalFormat;

/**
 * 数字处理工具类
 * 
 * @author wh
 * @since 0.0.1 
 */
public class NumberUtils {

    /**
     * 格式化数字
     * @param number
     * @param format
     * @return
     */
    public static String formatNumber(double number, String format) {
        return new DecimalFormat(format).format(number);
    }
}
