package com.aoyibuy.commons.passport;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;

import com.aoyibuy.commons.passport.accesstoken.AccessTokenGenerator;
import com.aoyibuy.commons.redis.RedisOps;
import com.google.common.collect.Lists;

public class RedisSessionService implements SessionService {
	
	private static final String REDIS_KEY = "session";
	
	private static final String SEPARATOR = ",";
	
	private String redisKey = REDIS_KEY; // session存储在redis中的前缀
	
	private long timeoutOfMinutes = 120; // session超时时间（单位分钟），默认为120分钟
    
    @Autowired
    private RedisOps redisOps;
    
    private HashOperations<String, String, String> hashOps;
    
    @Autowired
    private AccessTokenGenerator accessTokenGenerator;
    
	@Autowired
    private AccountConverter<?> accountConverter;
	
    @PostConstruct
    public void init() {
        hashOps = redisOps.getHashOps();
    }
    
    public void setRedisKey(String redisKey) {
		this.redisKey = redisKey;
	}
    
    public void setTimeoutOfMinutes(Long timeoutOfMinutes) {
		this.timeoutOfMinutes = timeoutOfMinutes;
	}

	@SuppressWarnings("rawtypes")
	@Override
    public void record(String accessToken, Account account) {
		account.setAccessToken(accessToken);
		String sessionToken = toSessionToken(accessToken);
		recordAccount(sessionToken, account);
        recordRoleAndPermission(sessionToken, account.getId());
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void recordAccount(String sessionToken, Account account) {
        hashOps.putAll(sessionToken, accountConverter.toMap(account));
        redisOps.expire(sessionToken, timeoutOfMinutes, TimeUnit.MINUTES); // 设置过期时间
	}

    private void recordRoleAndPermission(String sessionToken, String accountId) {
//        List<String> roleNames = permissionService.getRoleNamesByAccountId(accountId); // 通过账户ID查询角色名称
//        List<String> permissions = permissionService.getPermissionsByRoleNames(roleNames); // 通过账户ID查询角色名称
//        hashOps.put(sessionToken, Account.ROLES, StringUtils.join(roleNames, SEPARATOR));
//        hashOps.put(sessionToken, Account.PERMISSIONS, StringUtils.join(permissions, SEPARATOR));
    }
    
    @Override
	public void refresh(Account<?> account) {
    	String accountId = account.getId();
    	String sessionTokenPattern = redisKey + ":" + accountId + ":*";
    	Collection<String> sessionTokens = redisOps.getKeys(sessionTokenPattern);
    	if (CollectionUtils.isNotEmpty(sessionTokens)) {
    		for (String sessionToken : sessionTokens) {
    			String accessToken = retrieveInternal(sessionToken).getAccessToken();
    			record(accessToken, account);
    		}
    	}
	}
    
    @Override
    public void kickoutLoggedIn(String sessionToken) {
        if (redisOps.hasKey(sessionToken)) {
            redisOps.deleteKey(sessionToken);
        }
    }

	@Override
    public Account<?> retrieve(String accessToken) {
        String sessionToken = toSessionToken(accessToken);
        return retrieveInternal(sessionToken);
    }
	
	private Account<?> retrieveInternal(String sessionToken) {
		if (redisOps.hasKey(sessionToken)) {
            redisOps.expire(sessionToken, timeoutOfMinutes, TimeUnit.MINUTES); // 更新过期时间
            Map<String, String> entries = hashOps.entries(sessionToken);
            return accountConverter.fromMap(entries);
        }
        return null;
	}

    @Override
    public void invalidate(String accessToken) {
        String sessionToken = toSessionToken(accessToken);
        if (redisOps.hasKey(sessionToken)) {
            redisOps.deleteKey(sessionToken);
        }
    }

    @Override
    public Boolean isLogged(String accessToken) {
    	Account<?> account = retrieve(accessToken);
        return StringUtils.isNotEmpty(accessToken) && account != null && accessToken.equals(account.getAccessToken());
    }
    
    /**
     * 从访问令牌中解析出userId，并计算出sessionKey的值
     * @param accessToken
     * @return
     */
    private String toSessionToken(String accessToken) {
        return redisKey + ":" + accessTokenGenerator.decode(accessToken);
    }

    @Override
    public List<String> getRoles(String accessToken) {
        if (isLogged(accessToken)) {
        	String sessionToken = toSessionToken(accessToken);
        	String roles = hashOps.get(sessionToken, Account.ROLES);
        	return Arrays.asList(StringUtils.split(roles, SEPARATOR));
        }
        return Lists.newArrayListWithCapacity(0);
    }

    @Override
    public List<String> getPermissions(String accessToken) {
    	if (isLogged(accessToken)) {
    		String sessionToken = toSessionToken(accessToken);
            String permissions = hashOps.get(sessionToken, Account.PERMISSIONS);
            return Arrays.asList(StringUtils.split(permissions, SEPARATOR));
    	}
    	return Lists.newArrayListWithCapacity(0);
    }

	@Override
	public Boolean[] hasRoles(String accessToken, String... role) {
		List<String> roles = getRoles(accessToken);
		Boolean[] result = new Boolean[role.length];
		for (int i = 0; i < role.length; i++) {
			result[i] = roles.contains(role[i]);
		}
		return result;
	}
	
	@Override
	public Boolean[] hasPermissions(String accessToken, String... permisson) {
		List<String> permissions = getPermissions(accessToken);
		Boolean[] result = new Boolean[permisson.length];
		for (int i = 0; i < permisson.length; i++) {
			result[i] = permissions.contains(permisson[i]);
		}
		return result;
	}

}
