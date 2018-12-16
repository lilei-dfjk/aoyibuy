package com.aoyibuy.app.model.form;

import javax.validation.constraints.Pattern;

import lombok.Data;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class RegisterForm {

	@NotEmpty(message = "用户名不能为空")
	@Length(min = 4, max = 16, message = "用户名的长度必须在{min}-{max}之间")
	private String username;
	
	@NotEmpty(message = "密码不能为空")
	@Length(min = 6, max = 16, message = "密码的长度必须在{min}-{max}之间")
	private String password;
	
	@NotEmpty(message = "手机号不能为空")
	@Pattern(regexp = "^1[3|4|5|7|8]\\d{9}$", message = "手机号格式不正确")
	private String mobile;
	
	@NotEmpty(message = "短信验证码不能为空")
	@Length(min = 6, max = 6, message = "短信验证码必须为6位")
	private String smsCode;
	
	private String mail;

}
