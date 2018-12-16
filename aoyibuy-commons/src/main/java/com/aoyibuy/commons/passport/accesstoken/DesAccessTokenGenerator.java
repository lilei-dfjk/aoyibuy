package com.aoyibuy.commons.passport.accesstoken;

import java.util.Date;

import com.aoyibuy.commons.utils.EncryptionUtils;

/**
 * 通过DES算法对访问令牌进行加解密
 * 
 * @author wh
 * @lastModified 2016-2-3 16:27:32
 */
public class DesAccessTokenGenerator implements AccessTokenGenerator {
    
    private static final String SEPARATOR = ":";
    
    private String desKeySpec;

    public void setDesKeySpec(String desKeySpec) {
		this.desKeySpec = desKeySpec;
	}

    /**
     * Token格式: 'userId:时间戳', 通过DES算法加密, 再编码为BASE64
     */
    @Override
    public String encode(String userId) {
    	long randomStr = new Date().getTime();
    	return EncryptionUtils.desEncode(userId + SEPARATOR + randomStr, desKeySpec);
    }

    @Override
    public String decode(String accessToken) {
    	return EncryptionUtils.desDecode(accessToken, desKeySpec);
    }

}

