package com.aoyibuy.commons.exception;

@SuppressWarnings("serial")
public class DataAccessPermissionException extends AppException {

	public DataAccessPermissionException() {
        super();
    }

    public DataAccessPermissionException(String message) {
        super(message);
    }

    public DataAccessPermissionException(Throwable cause) {
        super(cause);
    }

    public DataAccessPermissionException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
