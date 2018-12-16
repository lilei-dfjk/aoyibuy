package com.aoyibuy.app.web.advice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.aoyibuy.app.web.exception.LoginRequiredException;
import com.aoyibuy.model.Response;

/**
 * MVC层的异常处理
 * 
 * @author wh
 * @since 0.0.1
 */
@ControllerAdvice
public class WebControllerAdvice {
    
    private static final Logger logger = LoggerFactory.getLogger(WebControllerAdvice.class);
    
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Response<String>> handleException(Throwable ex, HttpServletRequest request, HttpServletResponse response) {
        logger.error(ExceptionUtils.getStackTrace(ex));
        
        if (ex instanceof LoginRequiredException) {
            return renderResponse(Response.loginRequired());
        }
        
        return renderResponse(Response.caughtException(ex.getMessage()));
    }
    
    public ResponseEntity<Response<String>> renderResponse(Response<String> response) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("ContentType", "application/json;charset=UTF-8");
        return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
    }
    
}
