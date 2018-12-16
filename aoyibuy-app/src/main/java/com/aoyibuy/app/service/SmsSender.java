package com.aoyibuy.app.service;

public interface SmsSender {
	
	void sendSms(String mobile, String formattedContent);
	
	void sendSms(String mobile, String formattedContent, Object... args);
	
	void sendSmsWithCompleteContent(String mobile, String content);
	
}
