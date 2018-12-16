package com.aoyibuy.app.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aoyibuy.app.service.UserService;
import com.aoyibuy.app.web.method.support.CurrentUser;
import com.aoyibuy.model.Response;

@RequestMapping("/demo")
@RestController
public class DemoController {
    
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response<String> login(String username, String password) {
        String token = ""; // 登录成功计算出一个token
        userService.cache(token, username);
        return Response.success("", token);
    }
    
    /**
     * 执行业务操作, 要求必须登录. 会触发 CurrentUserMethodArgumentResolver
     * @param currentUser
     * @return
     */
    @RequestMapping("/biz")
    public Response<String> biz(CurrentUser currentUser) {
        logger.info("{}", currentUser);
        return Response.success();
    }
    
}
