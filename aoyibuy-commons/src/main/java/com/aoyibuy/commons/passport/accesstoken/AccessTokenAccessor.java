package com.aoyibuy.commons.passport.accesstoken;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class AccessTokenAccessor implements AccessTokenAware {
	
	private String accessTokenKey;
	
	public void setAccessTokenKey(String accessTokenKey) {
		this.accessTokenKey = accessTokenKey;
	}

    @Override
    public String getAccessToken(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String accessToken = getFromCookie(req);
        if (StringUtils.isEmpty(accessToken)) {
        	accessToken = getFromParameter(req);
        }
        if (StringUtils.isEmpty(accessToken)) {
        	accessToken = getFromAttribute(req);
        }
        if (StringUtils.isEmpty(accessToken)) {
        	accessToken = getFromHeader(req);
        }
        return StringUtils.defaultString(accessToken);
    }

	private String getFromCookie(HttpServletRequest req) {
    	Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (getAccessTokenKey().equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return StringUtils.EMPTY;
    }
    
    private String getFromParameter(HttpServletRequest req) {
    	return req.getParameter(getAccessTokenKey());
    }
    
    private String getFromAttribute(HttpServletRequest req) {
  		return (String) req.getAttribute(getAccessTokenKey());
  	}
    
    private String getFromHeader(HttpServletRequest req) {
    	return req.getHeader(getAccessTokenKey());
	}

	@Override
	public String getAccessTokenKey() {
		return accessTokenKey;
	}
    
}
