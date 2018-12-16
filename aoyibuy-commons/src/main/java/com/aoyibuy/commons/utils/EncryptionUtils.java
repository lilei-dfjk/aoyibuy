package com.aoyibuy.commons.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EncryptionUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(EncryptionUtils.class);
	
	public static String randomMd5() {
		return randomMd5(16, 128);
	}
	
	public static String randomMd5(int count) {
		return randomMd5(count, 128);
	}
	
	public static String randomMd5(int count, int hashIterations) {
		String randomStr = RandomStringUtils.randomAlphanumeric(count);
		return new Md5Hash(randomStr, randomStr, hashIterations).toBase64();
	}
	
    public static String desEncode(String strToEncrypt, String desKeySpec) {
		if (StringUtils.isEmpty(strToEncrypt) || StringUtils.isEmpty(desKeySpec)) {
			return "";
		}
		
        try {
            Cipher encryptCipher = Cipher.getInstance("DES");
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey sk = keyFactory.generateSecret(new DESKeySpec(desKeySpec.getBytes()));
            encryptCipher.init(Cipher.ENCRYPT_MODE, sk);
            return new String(Base64.encodeBase64(encryptCipher.doFinal(strToEncrypt.getBytes())));
        } catch (Exception e) {
        	logger.error(e.getMessage());
            return "";
        }
    }

    public static String desDecode(String encryptedStr, String desKeySpec) {
		if (StringUtils.isEmpty(encryptedStr) || StringUtils.isEmpty(desKeySpec)) {
			return "";
		}
		
        try {
            Cipher decryptCipher = Cipher.getInstance("DES");
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey sk = keyFactory.generateSecret(new DESKeySpec(desKeySpec.getBytes()));
            decryptCipher.init(Cipher.DECRYPT_MODE, sk);
            return new String(decryptCipher.doFinal(Base64.decodeBase64(encryptedStr.getBytes())));
        } catch (Exception e) {
        	logger.error(e.getMessage());
        	return "";
        }
    }
    
	public static String aesEncode(String strToEncrypt, String key) {
		if (StringUtils.isEmpty(strToEncrypt) || StringUtils.isEmpty(key)) {
			return "";
		}
		
		Key keySpec = new SecretKeySpec(key.getBytes(), "AES");
		try {
			Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, keySpec);
			return new String(Base64.encodeBase64(c.doFinal(strToEncrypt.getBytes())));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "";
		}
    }
	
	public static String aesDecode(String encryptedStr, String key) {
		if (StringUtils.isEmpty(encryptedStr) || StringUtils.isEmpty(key)) {
			return "";
		}
		
		Key keySpec = new SecretKeySpec(key.getBytes(), "AES");
		try {
			Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, keySpec);
			return new String(c.doFinal(Base64.decodeBase64(encryptedStr.getBytes())));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "";
		}
    }
	
}
