package com.aoyibuy.commons.utils;

import java.util.Arrays;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.aoyibuy.commons.exception.DataAccessPermissionException;
import com.aoyibuy.commons.exception.DataFormatConstraintsException;
import com.aoyibuy.commons.response.ResponseCode;


public class AssertUtils {
	
	public static void assertImage(String imageExtension) {
		assertTrue(CommonsUtils.isImage(imageExtension), "不是合法的图片后缀名");
	}
	
	public static void assertFileSize(Long fileSizeOfBytes, int sizeOfMB) {
		assertTrue(fileSizeOfBytes < (sizeOfMB * 1024 * 1024), "文件大小不能超过" + sizeOfMB + "MB");
	}
	
	public static void assertFileExtension(String fileExtension, String... extensionAllowed) {
		List<String> extensionsAllowed = Arrays.asList(extensionAllowed);
		assertTrue(extensionsAllowed.contains(fileExtension.toLowerCase()), "合法的文件后缀名包括: " + extensionsAllowed);
	}
	
	public static void assertTrue(boolean expression, String msg) {
		if (!expression) {
			throw new DataAccessPermissionException(msg);
		}
	}
	
	public static void assertFieldError(boolean expression, String field, String msg) {
		if (!expression) {
			throw new DataFormatConstraintsException(field, msg);
		}
	}
	
	public static void assertFieldError(boolean expression, ResponseCode rc) {
		assertFieldError(expression, rc.getHtmlElementName(), rc.getMsg());
	}
	
	public static void assertFieldErrors(Errors errors) {
		if (errors != null && errors.hasErrors()) {
			FieldError fieldError = errors.getFieldError();
			throw new DataFormatConstraintsException(
					fieldError.getField(), fieldError.getDefaultMessage());
		}
	}
	
}
