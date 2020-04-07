package com.jackson.service;

import com.jackson.model.SysUser;
import com.jackson.result.Results;

/**
 * 用户接口
 */
public interface UserService {

    /**
     * 依靠用户名查找用户信息
     */
    SysUser getUser(String username);

    /**
     * 用户注册
     * @return
     */
    Results register(String username,String password,Integer userType);

    /**
     * 用户退出
     */
    Results loginOut(String token);



}
