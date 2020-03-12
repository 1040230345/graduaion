package com.jackson.controller;

import com.jackson.result.Results;
import com.jackson.service.imp.UserServiceImp;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.logging.Logger;

@Controller
@Slf4j
@Api(tags = "测试接口")
public class TestController {
//    private Logger logger = LoggerFactory.getLogger(TestController.class);
//
//    @RequestMapping("/")
//    public String showHome() {
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("当前登陆用户：" + name);
//
//        return "home.html";
//    }
    @Autowired
    private UserServiceImp userServiceImp;

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @RequestMapping("/admin")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String printAdmin() {
        return "如果你看见这句话，说明你有ROLE_ADMIN角色";
    }

    @RequestMapping("/user")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public String printUser(String test) {
        System.out.println(test);
        return "如果你看见这句话，说明你有ROLE_USER角色";
    }

//    @RequestMapping("/test")
//    @ResponseBody
//    public Results testController(){
//
//        return userServiceImp.testService();
//    }

}
