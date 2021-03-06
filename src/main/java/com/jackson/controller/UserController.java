package com.jackson.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jackson.model.SysUser;
import com.jackson.result.Results;
import com.jackson.security.constants.SecurityConstants;
import com.jackson.security.utils.JwtTokenUtils;
import com.jackson.service.SysUserRoleService;
import com.jackson.service.UserService;
import com.jackson.threadLocal.RequestHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@ResponseBody
@Api(tags = "用户接口")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     */
    @GetMapping("/user/getUser")
    @ApiOperation("获取用户信息")
    public Results getUser(HttpServletRequest request){
        //获取username
        String username = JwtTokenUtils.getUsernameByRequest(request);

        return userService.getUserMsg(username);

    }

    /**
     * 用户注册
     */
    @PostMapping("/addUser")
    @ApiOperation("用户注册")
    public Results addUser(String username,String password,Integer userType,HttpServletRequest request){
        return userService.register(username, password, userType,request);
    }

    /**
     * 用户退出
     */
    @PostMapping("/user/loginOut")
    @ApiOperation("用户退出")
    public Results loginOut(HttpServletRequest request){
        //从头部中获取token
        String authorization = request.getHeader(SecurityConstants.TOKEN_HEADER);
        String token = authorization.replace(SecurityConstants.TOKEN_PREFIX, "");
        return userService.loginOut(token);
    }

    /**
     * 更改用户信息
     */
    @PutMapping("/user/userMsg")
    @ApiOperation("更改用户信息")
    public Results updateUserMsg(@RequestBody SysUser sysUser){

        //获取用户username
        String username = (String) RequestHolder.getId();

        sysUser.setPassword(new BCryptPasswordEncoder().encode(sysUser.getPassword()));

        if (userService.update(sysUser,new QueryWrapper<SysUser>().eq("username",username))) {
            return Results.success();
        }

        return Results.failure();
    }

    /**
     * 获取做过的实验
     */
    @GetMapping("/user/userCurr")
    @ApiOperation("获取个人做过的实验")
    public Results getUserCurr(){
        //获取用户username
        String username = (String) RequestHolder.getId();

        return userService.getUserCurr(username);
    }

    /**
     * 删除做过的实验
     */
    @DeleteMapping("/user/userCurr")
    @ApiOperation("删除做过的实验")
    public Results delUserCurr(Integer userCurrId){
        return userService.delUserCurr(userCurrId);
    }


}
