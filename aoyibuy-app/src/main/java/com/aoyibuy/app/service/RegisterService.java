package com.aoyibuy.app.service;

import com.aoyibuy.app.model.form.RegisterForm;
import com.aoyibuy.dao.jpa.entity.Member;


public interface RegisterService {
	
	String generateRandomRegisterToken();
	
	boolean checkSmsInterval(String registerToken);

	void sendSms(String registerToken, String mobile);

	boolean checkMobileSmsCode(String registerToken, String mobile,
			String smsCode);
 
	Member register(String registerToken, RegisterForm registerForm);

}
