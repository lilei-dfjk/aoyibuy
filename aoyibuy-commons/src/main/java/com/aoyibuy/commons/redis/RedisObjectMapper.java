package com.aoyibuy.commons.redis;

import java.util.Map;

public interface RedisObjectMapper<T> {

	Map<String, String> toMap(T t);
    
	T fromMap(Map<String, String> map);
	
}
