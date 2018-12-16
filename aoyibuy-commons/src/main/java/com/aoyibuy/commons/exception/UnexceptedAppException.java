package com.aoyibuy.commons.exception;

@SuppressWarnings("serial")
public class UnexceptedAppException extends AppException {
	
	public UnexceptedAppException() {
	}

	public UnexceptedAppException(Exception e) {
		super(e);
	}

}
