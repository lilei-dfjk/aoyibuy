package com.aoyibuy.commons.utils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpServet相关的工具类
 * 
 * @author wh
 * @since 0.0.1
 */
public abstract class HttpServletUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpServletUtils.class);

    public static final Map<String, String> resolveRequestParameter(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        Map<String, String> parameters = new HashMap<String, String>();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            parameters.put(parameterName, request.getParameter(parameterName));
        }
        return parameters;
    }

    /**
     * 读取request流
     * 
     * @param req
     * @return
     * @author ll
     */
    public static String readRequestBody(HttpServletRequest request) {
        try {
            return IOUtils.toString(request.getInputStream(), "utf-8");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void addCookieToResponse(HttpServletRequest request, HttpServletResponse response, 
            String cookieName, String cookieValue) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(-1); // 浏览器关闭时删除
        setDomainName(cookie, request, response);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void deleteCookieFromResponse(HttpServletRequest request,
            HttpServletResponse response, String cookieName) {
        Cookie registerTokenCookieToClear = new Cookie(cookieName, null);
        registerTokenCookieToClear.setMaxAge(0); // 立刻删除
        setDomainName(registerTokenCookieToClear, request, response);
        registerTokenCookieToClear.setPath("/");
        response.addCookie(registerTokenCookieToClear);
    }
    
    public static String getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(cookieName)) {
                    return c.getValue();
                }
            }
        }
        return null;
    }

    private static final void setDomainName(Cookie cookie, HttpServletRequest request, 
            HttpServletResponse response) {
        String domainName = determineDomainName(request);
        if (!"localhost".equals(domainName)) {
            cookie.setDomain(domainName);
        }
    }

    /**
     * https://www.google.com/search?q=abc -> .google.com
     * https://google.com/search?q=abc -> .google.com
     * https://www.google.com.hk/search?q=abc -> .google.com.hk
     * http://localhost:8080/eastlending-fisp-admin/login -> localhost
     */
    private static final String determineDomainName(HttpServletRequest request) {
        String domainName = null;

        String url = request.getRequestURL().toString();
        if (org.apache.commons.lang3.StringUtils.isEmpty(url)) {
            domainName = "";
        } else {
            // 例如：
            // url=https://www.google.com/search?q=abc
            // serverName=www.google.com
            String serverName = org.apache.commons.lang3.StringUtils.substringBetween(url, "://", "/");
            final String[] domains = serverName.split("\\.");
            int parts = domains.length;
            if (parts > 3) {
                // www.xxx.com.cn
                domainName = "." + domains[parts - 3] + "."
                        + domains[parts - 2] + "." + domains[parts - 1];
            } else if (parts <= 3 && parts > 1) {
                // xxx.com or xxx.cn
                domainName = "." + domains[parts - 2] + "." + domains[parts - 1];
            } else {
                domainName = serverName;
            }
        }

        if (domainName != null && domainName.indexOf(":") > 0) {
            domainName = domainName.split("\\:")[0];
        }
        return domainName;
    }

    public static boolean isAsync(HttpServletRequest request) {
        if (logger.isDebugEnabled()) {
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String header = headerNames.nextElement();
                logger.debug("{} - {}", header, request.getHeader(header));
            }
        }

        if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
            return true;
        }

        // 系统中所有上传都是异步的, 兼容低版本IE在异步上传时无x-requested-with
        if (Objects.toString(request.getHeader("content-type"), "").startsWith("multipart/form-data")) {
            return true;
        }

        return false;
    }

}
