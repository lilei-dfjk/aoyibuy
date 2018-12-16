package com.aoyibuy.commons.redis;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

/**
 * 包装StringDataRedis支持的redis操作
 * 
 * @author wh
 * @since 0.0.1
 */
@Getter
public class RedisOps {

    @Setter
    private StringRedisTemplate stringRedisTemplate;

    private HashOperations<String, String, String> hashOps;

    private HyperLogLogOperations<String, String> hyperLogLogOps;

    private ListOperations<String, String> listOps;

    private SetOperations<String, String> setOps;

    private ValueOperations<String, String> valueOps;

    private ZSetOperations<String, String> zsetOps;
    
    @PostConstruct
    public void setUp() {
        hashOps = stringRedisTemplate.opsForHash();
        hyperLogLogOps = stringRedisTemplate.opsForHyperLogLog();
        listOps = stringRedisTemplate.opsForList();
        setOps = stringRedisTemplate.opsForSet();
        valueOps = stringRedisTemplate.opsForValue();
        zsetOps = stringRedisTemplate.opsForZSet();
    }
    
    public Boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }
    
    public void deleteKey(String... keys) {
        stringRedisTemplate.delete(Arrays.asList(keys));
    }
    
    public void expire(String key, Long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.expire(key, timeout, timeUnit);
    }
    
    public Collection<String> getKeys(String pattern) {
        return stringRedisTemplate.keys(pattern);
    }
    
    public Long ttl(String key) {
        return stringRedisTemplate.getExpire(key);
    }
    
    public Long ttl(String key, TimeUnit timeUnit) {
        return stringRedisTemplate.getExpire(key, timeUnit);
    }

}
