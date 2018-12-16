package com.aoyibuy.commons.exception;

/**
 * 应用异常包装类
 * 
 * @author wh
 * @lastModified 2016-5-18 18:11:36
 */
public abstract class AppException extends RuntimeException {

	private static final long serialVersionUID = 6263403498810901559L;
	
	public AppException() {
		super();
	}

	public AppException(String message) {
		super(message);
	}

	public AppException(Throwable cause) {
		super(cause);
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}
	
}