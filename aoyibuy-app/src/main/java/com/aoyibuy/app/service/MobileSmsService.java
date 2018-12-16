package com.aoyibuy.app.service;

import java.util.concurrent.TimeUnit;

import mapper.MobileSms;


public interface MobileSmsService {

	void recordMobileSms(String token, String mobile, String smsCode, String encryptedToken);
	
	void recordMobileSms(String token, MobileSms mobileSms, long timeout);

	void recordMobileSms(String token, MobileSms mobileSms, long timeout,
			TimeUnit timeunit);

	void recordMobileSms(String token, MobileSms mobileSms);

	MobileSms retriveMobileSms(String token);

	boolean checkSmsInterval(String token);
	
	boolean checkSmsInterval(String token, int betweenAtLeastInSeconds);

	boolean checkMobileSmsCode(String token, String mobile, String smsCode,
			String encryptedToken);
	
	void deleteMobileSms(String token);

	boolean checkMobileSmsKeyExists(String mobileSmsKey);

	String getMobileSmsKey(String token);

}
