package com.aoyibuy.model;

@SuppressWarnings("serial")
public final class Response<T> extends BaseModel {
    
    private int code;
    private String message;
    private T result;
    
    private Response() {
    }
    
    private Response(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }
    
    public static <T> Response<T> response(int code, String message, T result) {
        return new Response<T>(code, message, result);
    }
    
    public static <T> Response<T> success() {
        return response(200, "", null);
    }
    
    public static <T> Response<T> success(T result) {
        return response(200, "", result);
    }
    
    public static <T> Response<T> success(String message) {
        return response(200, message, null);
    }
    
    public static <T> Response<T> success(String message, T result) {
        return response(200, message, result);
    }
    
    public static <T> Response<T> loginRequired() {
        return response(-2, "请登录", null);
    }
    
    public static <T> Response<T> caughtException() {
        return response(-100, "服务端异常", null);
    }
    
    public static <T> Response<T> caughtException(String message) {
        return response(-100, message, null);
    }
    
    public boolean isSuccess() {
        return getCode() == 200;
    }
    
    public int getCode() {
        return code;
    }

    public T getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
    
}