package com.aoyibuy.app.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aoyibuy.app.model.RegisterResponseCode;
import com.aoyibuy.app.model.form.RegisterForm;
import com.aoyibuy.app.service.RegisterService;
import com.aoyibuy.app.service.UserService;
import com.aoyibuy.app.web.method.support.CurrentUser;
import com.aoyibuy.commons.utils.AssertUtils;
import com.aoyibuy.commons.utils.HttpServletUtils;
import com.aoyibuy.dao.jpa.entity.Member;
import com.aoyibuy.model.Response;

@RequestMapping("/register")
@RestController
public class RegisterController {
    
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    
    private static final String FRONT_REGISTER_TOKEN = "frt";

    @Autowired
    private UserService userService;
    
    @Autowired
    private RegisterService registerService;
    
    /**
	 * 发送短信验证码
	 * @param mobile
	 * @param captcha
	 * @param registerToken
	 * @return
	 */
	@RequestMapping(value = "/mobile/{mobile}/captcha/{captcha}", method = RequestMethod.GET)
	@ResponseBody
	public Response sendSmsCode(@PathVariable String mobile, @PathVariable String captcha,
			@CookieValue(FRONT_REGISTER_TOKEN) String registerToken) {
//		boolean correct = registerService.checkCaptchaImage(registerToken, captcha);
//		AssertUtils.assertFieldError(correct, RegisterResponseCode.CAPTCHA_ERROR);
		
		boolean canSend = registerService.checkSmsInterval(registerToken);
		AssertUtils.assertFieldError(canSend, RegisterResponseCode.SEND_SMS_FREQUENTLY);
		
		registerService.sendSms(registerToken, mobile);
		return Response.success();
	}
	
	/**
	 * 注册
	 * @param registerToken
	 * @param registerForm
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Response register(@CookieValue(FRONT_REGISTER_TOKEN) String registerToken, 
			@Valid RegisterForm registerForm, Errors errors, 
			HttpServletRequest request, HttpServletResponse response) {
		AssertUtils.assertFieldErrors(errors);
		
//		boolean usernameRegistered = registerService.checkUsernameRegistered(registerForm.getUsername());
//		AssertUtils.assertFieldError(!usernameRegistered, RegisterResponseCode.USERNAME_REGISTERED);
		
		boolean mobileSmsCodeCorrect = registerService.checkMobileSmsCode(registerToken, 
					registerForm.getMobile(), registerForm.getSmsCode());
		AssertUtils.assertFieldError(mobileSmsCodeCorrect, RegisterResponseCode.SMS_CODE_ERROR);
		
		Member member = doRegister(registerToken, registerForm, request, response); // 执行会员注册
//		autoLoginAfterRegistration(member, request, response); // 注册后自动登录
		return Response.success();
	}
	
	/**
	 * 执行注册
	 * @param registerToken
	 * @param registerForm
	 * @param response
	 * @return
	 */
	private Member doRegister(String registerToken, RegisterForm registerForm, 
			HttpServletRequest request, HttpServletResponse response) {
		HttpServletUtils.deleteCookieFromResponse(request, response, FRONT_REGISTER_TOKEN);
		return registerService.register(registerToken, registerForm); // 会员注册
	}
	
	
    
}
