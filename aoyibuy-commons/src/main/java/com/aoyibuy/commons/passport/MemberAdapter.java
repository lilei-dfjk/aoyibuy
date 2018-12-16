package com.aoyibuy.commons.passport;

import java.util.HashMap;
import java.util.Map;

import com.aoyibuy.dao.jpa.entity.Member;


/**
 * Member对象的适配器，使成为Account接口的实现
 * 
 * @author wh
 * @lastModified 2016-9-18 11:07:00
 */
public class MemberAdapter implements Account<Member> {
	
	private static final long serialVersionUID = 8646325066680928477L;

	private String accessToken;
	
	private Member member;
	
	private Map<String, String> extProperties = new HashMap<>(); // 扩展属性
	
	public MemberAdapter(Member member) {
		this.member = member;
	}
	
	public MemberAdapter(Member member, String accessToken) {
		this(member);
		this.accessToken = accessToken;
	}
	
	public MemberAdapter(Member member, String accessToken, Map<String, String> extProperties) {
		this(member, accessToken);
		this.extProperties = extProperties;
	}
	
	@Override
	public String getId() {
		return member.getId();
	}
	
	@Override
	public String getAccountName() {
		return member.getAccount();
	}
	
	@Override
	public String getName() {
		return "";
	}

	@Override
	public String getPassword() {
		return member.getPassword();
	}
	
	@Override
	public String getSalt() {
		return member.getSalt();
	}

	@Override
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public String getAccessToken() {
		return this.accessToken;
	}
	
	public void addExtProperties(String propertyName, String propertyValue) {
		extProperties.put(propertyName, propertyValue);
	}
	
	public void addAllExtProperties(Map<String, String> extProperties) {
		extProperties.putAll(extProperties);
	}
	
	public void setExtProperties(Map<String, String> extProperties) {
		this.extProperties = extProperties;
	}
	
	public Map<String, String> getExtProperties() {
		return extProperties;
	}
	
	@Override
	public void wrap(Member member) {
		this.member = member;
	}
	
	@Override
	public Member unwrap() {
		return member;
	}
	
	public Member getMember() {
		return unwrap();
	}
	
}
