package com.aoyibuy.app.model;

import com.aoyibuy.commons.response.ResponseCode;


public enum ChangePasswordResponseCode implements ResponseCode {
	 
	CAPTCHA_ERROR("captcha", "图片验证码填写错误"), 
	OLD_PASSWORD_ERROR("oldPassword", "旧密码填写错误");
	
	private String htmlElementName;
	private String msg;

	private ChangePasswordResponseCode(String htmlElementName, String msg) {
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
