package com.aoyibuy.commons.utils;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.util.ReflectionUtils;

public abstract class CommonsUtils {
	
	public static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "bmp", "gif");
	
	public static String extensionOf(String fileName) {
		return StringUtils.substringAfterLast(fileName, ".").toLowerCase();
	}
	
	public static boolean isImage(String imageExtension) {
		return IMAGE_EXTENSIONS.contains(imageExtension.toLowerCase());
	}
	
	public static boolean isPdf(String pdfExtension) {
		return "pdf".equals(pdfExtension.toLowerCase());
	}
	
	/**
	 * 将带文字说明的枚举转换为map
	 * @param enumClass 枚举类
	 * @param textField text字段名
	 * @return
	 */
	public static <E extends Enum<E>> Map<String, String> getEnumMap(final Class<E> enumClass, String textField) {
        final Map<String, String> map = new LinkedHashMap<String, String>();
        for (final E e: enumClass.getEnumConstants()) {
        	Method getter = ReflectionUtils.findMethod(e.getDeclaringClass(), "get" + StringUtils.capitalize(textField));
        	Object getterReturnValue = ReflectionUtils.invokeMethod(getter, e);
            map.put(e.name(), Objects.toString(getterReturnValue, ""));
        }
        return map;
	}
	
	/**
	 * 将带文字说明的枚举转换为map，字段名默认为text
	 * @param enumClass 枚举类
	 * @return
	 */
	public static <E extends Enum<E>> Map<String, String> getEnumMap(final Class<E> enumClass) {
        return getEnumMap(enumClass, "text");
	}
	
	public static String formatNumber(Double number) {
		return formatNumber(number, "#,##0.00");
	}
	
	public static String formatNumber(Double number, String format) {
		if (number == null || StringUtils.isEmpty(format)) {
			return "";
		}
		return new DecimalFormat(format).format(number);
	}
	
	public static String formatDate(Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}
	
	public static String formatDateTime(Date dateTime) {
		return formatDate(dateTime, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String formatDate(Date date, String format) {
		return new DateTime(date).toString(format);
	}
	
	public static Date parseDate(String date) {
		return parseDate(date, "yyyy-MM-dd");
	}
	
	public static Date parseDateTime(String dateTime) {
		return parseDate(dateTime, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static Date parseDate(String date, String format) {
		return DateTime.parse(date, DateTimeFormat.forPattern(format)).toDate();    
	}
	
	public static <T> T defaultIfNull(T sourceObject, T defaultObject) {
		return sourceObject != null ? sourceObject : defaultObject;
	}
}
