package com.aoyibuy.commons.springside;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Hex;

public class Encodes {
	private static final String DEFAULT_URL_ENCODING = "UTF-8";
	private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
	private static final String key = "mo961811a1122";
	public static String encode(String input) throws UnsupportedEncodingException {
		String str = input + key;
		return encodeHex(str.getBytes(DEFAULT_URL_ENCODING));
	}
	
	/**
	 * Hex编码.
	 */
	public static String encodeHex(byte[] input) {
		return Hex.encodeHexString(input);
	}

}
