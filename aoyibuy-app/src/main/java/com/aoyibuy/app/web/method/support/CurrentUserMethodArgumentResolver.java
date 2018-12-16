package com.aoyibuy.app.web.method.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.aoyibuy.app.web.exception.LoginRequiredException;
import com.aoyibuy.commons.redis.RedisKeys;
import com.aoyibuy.commons.redis.RedisOps;
import com.aoyibuy.commons.utils.JsonUtils;


public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private RedisOps redisOps;
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        try {
            String token = webRequest.getParameter("token");
            
            Assert.hasLength(token);
            
            String userJson = redisOps.getValueOps().get(RedisKeys.userToken(token));
            CurrentUser currentUser = JsonUtils.fromJson(userJson, CurrentUser.class);
            
            Assert.notNull(currentUser);
            
            return currentUser;
        } catch (Exception e) {
            throw new LoginRequiredException();
        }
    }

}
