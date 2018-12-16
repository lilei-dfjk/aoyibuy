package com.aoyibuy.app.model;

import com.aoyibuy.commons.response.ResponseCode;


public enum RegisterResponseCode implements ResponseCode {
	
	USERNAME_REGISTERED("username", "用户名已注册，请登录"),
	CAPTCHA_ERROR("captcha", "图片验证码填写错误"),
	SEND_SMS_FREQUENTLY("smsCode", "发送短信操作太频繁，间隔不能超过1分钟"),
	SMS_CODE_ERROR("smsCode", "短信验证码填写错误");

	private String htmlElementName;
	private String msg;

	private RegisterResponseCode(String htmlElementName, String msg) {
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