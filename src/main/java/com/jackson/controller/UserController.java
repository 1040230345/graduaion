package com.jackson.controller;

import com.jackson.result.Results;
import com.jackson.security.constants.SecurityConstants;
import com.jackson.security.utils.JwtTokenUtils;
import com.jackson.service.SysUserRoleService;
import com.jackson.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
@ResponseBody
@Api(tags = "用户接口")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     */
    @GetMapping("/getUser")
    @ApiOperation("获取用户信息")
    public Results getUser(HttpServletRequest request){
        //获取username
        String username = JwtTokenUtils.getUsernameByRequest(request);
        //获取用户信息
        return Results.success(userService.getUser(username));

    }

    /**
     * 用户注册
     */
    @PostMapping("/addUser")
    @ApiOperation("用户注册")
    public Results addUser(String username,String password,Integer userType){
        return userService.register(username, password, userType);
    }

    /**
     * 用户退出
     */
    @PostMapping("/loginOut")
    @ApiOperation("用户退出")
    public Results loginOut(HttpServletRequest request){
        //从头部中获取token
        String authorization = request.getHeader(SecurityConstants.TOKEN_HEADER);
        String token = authorization.replace(SecurityConstants.TOKEN_PREFIX, "");
        return userService.loginOut(token);
    }

    /**
     * 更换头像
     */
    @PutMapping("/userImage")
    @ApiOperation("更改用户头像")
    public Results updateUserImage(){
        return null;
    }


}
