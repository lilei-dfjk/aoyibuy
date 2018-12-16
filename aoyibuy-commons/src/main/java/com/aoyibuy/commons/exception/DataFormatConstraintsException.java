package com.aoyibuy.commons.exception;

/**
 * 数据格式约束异常
 * 发生此异常的可能性有两种：
 * 1. 基本格式校验不正确，如长度、必填等
 * 2. 数据校验不正确，如验证码校验错误等
 * 
 * @author wh
 * @lastModified 2016-9-8 13:40:33
 */
public class DataFormatConstraintsException extends AppException {

	private static final long serialVersionUID = -2571509156480491716L;
	
	private String field;
	
	public DataFormatConstraintsException(String field, String message) {
		super(message);
		this.field = field;
	}
	
	public String getField() {
		return this.field;
	}

}
