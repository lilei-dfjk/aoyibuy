package com.aoyibuy.commons.redis;

/**
 * 所有redis的key统一写在这里
 * 
 * @author wh
 * @since 0.0.1
 */
public abstract class RedisKeys {
    
    private static final String DELIMITER = ":"; // 分隔符
    
    private static final String USER_TOKEN = "aoyibuy:userToken"; // 用户登录令牌
    
    public static String userToken(String token) {
        return USER_TOKEN + DELIMITER + token;
    }

}
