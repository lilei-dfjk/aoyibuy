package com.aoyibuy.app.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aoyibuy.commons.redis.RedisKeys;
import com.aoyibuy.commons.redis.RedisOps;
import com.aoyibuy.commons.utils.JsonUtils;
import com.aoyibuy.dao.jpa.entity.User;
import com.aoyibuy.dao.jpa.repo.UserRepo;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisOps redisOps;
    
    @Autowired
    private UserRepo userRepo;
    
    @Override
    public void cache(String token, String username) {
        User user = userRepo.findByName(username);
        redisOps.getValueOps().set(RedisKeys.userToken(token), JsonUtils.toJson(user), 1L, TimeUnit.DAYS);
    }
    
}
