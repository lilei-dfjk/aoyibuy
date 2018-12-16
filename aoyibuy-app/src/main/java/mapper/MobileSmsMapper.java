package mapper;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.aoyibuy.commons.redis.RedisObjectMapper;
import com.google.common.collect.ImmutableMap;

@Component("mobileSmsMapper")
public class MobileSmsMapper implements RedisObjectMapper<MobileSms> {
	
	private static final String MOBILE = "mobile";
	private static final String SMS_CODE = "smsCode";
	private static final String LAST_SEND_AT = "lastSendAt";
	private static final String ENCRYPTED_TOKEN = "encryptedToken";

	@Override
	public Map<String, String> toMap(MobileSms mobileSms) {
		return ImmutableMap.<String, String>builder()
				.put(MOBILE, StringUtils.defaultString(mobileSms.getMobile()))
				.put(SMS_CODE, StringUtils.defaultString(mobileSms.getSmsCode()))
				.put(LAST_SEND_AT, Objects.toString(mobileSms.getLastSendAt(), ""))
				.put(ENCRYPTED_TOKEN, StringUtils.defaultString(mobileSms.getEncryptedToken()))
				.build();
	}

	@Override
	public MobileSms fromMap(Map<String, String> map) {
		MobileSms mobileSms = new MobileSms();
		mobileSms.setMobile(map.get(MOBILE));
		mobileSms.setSmsCode(map.get(SMS_CODE));
		mobileSms.setLastSendAt(Long.valueOf(map.get(LAST_SEND_AT)));
		mobileSms.setEncryptedToken(map.get(ENCRYPTED_TOKEN));
		return mobileSms;
	}

}
