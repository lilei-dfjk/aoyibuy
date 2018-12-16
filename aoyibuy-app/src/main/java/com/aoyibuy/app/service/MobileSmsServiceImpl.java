package com.aoyibuy.app.service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import mapper.MobileSms;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.aoyibuy.commons.redis.RedisObjectMapper;
import com.aoyibuy.commons.redis.RedisOps;

public abstract class MobileSmsServiceImpl implements MobileSmsService {
	
	public static final String MAPPER_BEAN = "mobileSmsMapper";
	
	@Autowired
	private RedisOps redisOps;
	
	@Resource(name = MAPPER_BEAN)
	private RedisObjectMapper<MobileSms> mobileSmsMapper;
	
	@Override
	public void recordMobileSms(String token, String mobile, String smsCode, String encryptedToken) {
		MobileSms mobileSms = new MobileSms();
		mobileSms.setMobile(mobile);
		mobileSms.setSmsCode(smsCode);
		mobileSms.setLastSendAt(new Date().getTime());
		mobileSms.setEncryptedToken(encryptedToken);
		recordMobileSms(token, mobileSms);
	}
	
	@Override
	public String getMobileSmsKey(String token) {
		return getRedisKeyPrefix() + token;
	}
	
	protected abstract String getRedisKeyPrefix();
	
	@Override
	public void recordMobileSms(String token, MobileSms mobileSms) {
		// 短信验证码超时时间为10分钟
		recordMobileSms(token, mobileSms, 10L);
	}
	
	@Override
	public void recordMobileSms(String token, MobileSms mobileSms, long timeout) {
		recordMobileSms(token, mobileSms, timeout, TimeUnit.MINUTES);
	}
	
	@Override
	public void recordMobileSms(String token, MobileSms mobileSms, long timeout, 
			TimeUnit timeunit) {
		String mobileSmsKey = getMobileSmsKey(token);
		redisOps.getHashOps().putAll(mobileSmsKey, mobileSmsMapper.toMap(mobileSms));
		redisOps.expire(mobileSmsKey, timeout, timeunit);
	}
	
	@Override
	public MobileSms retriveMobileSms(String token) {
		String mobileSmsKey = getMobileSmsKey(token);
		if (checkMobileSmsKeyExists(mobileSmsKey)) {
			Map<String, String> mobileSmsMap = redisOps.getHashOps().entries(mobileSmsKey);
			return mobileSmsMapper.fromMap(mobileSmsMap);
		}
		return null;
	}
	
	@Override
	public boolean checkMobileSmsCode(String token, String mobile, String smsCode, 
			String encryptedToken) {
		MobileSms mobileSms = retriveMobileSms(token);
		if (mobileSms != null) {
			return StringUtils.equals(mobileSms.getMobile(), mobile) && 
					StringUtils.equals(mobileSms.getSmsCode(), smsCode) &&
					StringUtils.equals(mobileSms.getEncryptedToken(), encryptedToken); // 防伪造
		}
		return false;
	}
	
	@Override
	public boolean checkSmsInterval(String token) {
		MobileSms mobileSms = retriveMobileSms(token);
		if (mobileSms != null) {
			Long lastSendAt = mobileSms.getLastSendAt();
			long now = new Date().getTime();
			return (now - lastSendAt) > 60 * 1000L; // 距上次发送短信超过60秒
		}
		return true;
	}
	
	@Override
	public boolean checkSmsInterval(String token, int betweenAtLeastInSeconds) {
		MobileSms mobileSms = retriveMobileSms(token);
		if (mobileSms != null) {
			Long lastSendAt = mobileSms.getLastSendAt();
			long now = new Date().getTime();
			return (now - lastSendAt) > betweenAtLeastInSeconds * 1000L;
		}
		return true;
	}
	
	@Override
	public void deleteMobileSms(String token) {
		String mobileSmsKey = getMobileSmsKey(token);
		if (checkMobileSmsKeyExists(mobileSmsKey)) {
			redisOps.deleteKey(mobileSmsKey);
		}
	}
	
	@Override
	public boolean checkMobileSmsKeyExists(String mobileSmsKey) {
		return redisOps.hasKey(mobileSmsKey);
	}
}
