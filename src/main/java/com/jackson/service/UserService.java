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
     * @param umsAdminParam
     * @return
     */
    SysUser register(SysUser umsAdminParam);

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    String login(String username,String password);

    /**
     * 用户退出
     */
    Results loginOut(String token);



}
