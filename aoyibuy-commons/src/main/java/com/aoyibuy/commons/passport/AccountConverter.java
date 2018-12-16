package com.aoyibuy.commons.passport;

import java.util.Map;

/**
 * <p>账户转换器。由于账户信息需要以hash类型存储在Redis中，因此需要实现账户对象与Map<String, String>类型的相互转换
 * 
 * <p>可由开发者根据需要，自定义需要转换的列
 * 
 * @author Wh
 * @lastModified 2015-7-1 11:57:51
 */
public interface AccountConverter<T> {
	
	String ACCESS_TOKEN = "accessToken"; // 访问令牌
	String ID = "id"; // 账户ID
	String ACCOUNT = "account"; // 登录名
	String NAME = "name"; // 账户ID
	String ROLES = "roles"; // 角色
	String PERMISSIONS = "permissions"; // 权限
    
    Map<String, String> toMap(Account<T> account);
    
    Account<T> fromMap(Map<String, String> map);
    
}
