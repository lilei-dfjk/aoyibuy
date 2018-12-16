package com.aoyibuy.app.service;

import org.springframework.stereotype.Service;

@Service(RegisterMobileSmsImpl.BEAN)
public class RegisterMobileSmsImpl extends MobileSmsServiceImpl {
	
	public static final String BEAN = "front.registerMobileSms";

	@Override
	protected String getRedisKeyPrefix() {
		return "aoyibuy:front:register:sms:";
	}

}
