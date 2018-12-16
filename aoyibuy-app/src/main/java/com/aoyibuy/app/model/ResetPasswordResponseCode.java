package com.aoyibuy.app.model;

import com.aoyibuy.commons.response.ResponseCode;


public enum ResetPasswordResponseCode implements ResponseCode {
	
	USERNAME_UNREGISTERED("username", "用户未注册"),
	CAPTCHA_ERROR("captcha", "图片验证码填写错误"),
	SEND_SMS_FREQUENTLY("smsCode", "发送短信操作太频繁，间隔不能超过1分钟"),
	SMS_CODE_ERROR("smsCode", "短信验证码填写错误"),
	INVALID_COOKIE(OTHER, "cookie校验失败");

	private String htmlElementName;
	private String msg;

	private ResetPasswordResponseCode(String htmlElementName, String msg) {
		this.htmlElementName = htmlElementName;
		this.msg = msg;
	}
	
	@Override
	public String getHtmlElementName() {
		return htmlElementName;
	}

	@Override
	public String getMsg() {
		return msg;
	}
	
}
