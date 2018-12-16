package com.aoyibuy.app.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SmsSenderExecutor implements SmsSender {
	
	private static final Logger logger = LoggerFactory.getLogger(SmsSenderExecutor.class);
	
	
	@Override
	public void sendSms(String mobile, String formattedContent) {
		String smsCode = RandomStringUtils.randomNumeric(6); // 6位随机数
		sendSms(mobile, formattedContent, smsCode);
	}
	
	@Override
	public void sendSms(String mobile, String formattedContent, Object... args) {
		String content = String.format(formattedContent, args);
		sendSmsWithCompleteContent(mobile, content);
	}
	
	@Override
	public void sendSmsWithCompleteContent(String mobile, String content) {
		logger.info("向{}发送短信：{}", mobile, content);
//		SmsData smsData = new SmsData();
//		smsData.setMobile(mobile);
//		smsData.setContent(content);
//		messageProducer.sendSms(smsData);
	}

}
