package com.aoyibuy.commons.exception;

@SuppressWarnings("serial")
public class DubboRpcProviderException extends AppException {
	
	public DubboRpcProviderException() {
	}
	
    public DubboRpcProviderException(String message) {
        super(message);
    }

	public DubboRpcProviderException(Exception e) {
		super(e);
	}

}
