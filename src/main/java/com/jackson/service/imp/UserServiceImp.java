package com.jackson.service.imp;

import com.jackson.dao.UserDao;
import com.jackson.exception.CustomizeErrorCode;
import com.jackson.exception.CustomizeException;
import com.jackson.model.SysUser;
import com.jackson.result.Results;
import com.jackson.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 用户操作事务层
 */

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取用户
     * @param username
     * @return
     */
    @Override
    public SysUser getUser(String username) {
        return userDao.getUser(username);
    }

    /**
     * 用户注册
     * @param umsAdminParam
     * @return
     */
    @Override
    public SysUser register(SysUser umsAdminParam) {
        return null;
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public String login(String username, String password) {
        return null;
    }

    /**
     * 用户退出
     * @param token
     * @return
     */
    @Override
    public Results loginOut(String token) {
        stringRedisTemplate.opsForSet().add("BlackToken", token);//向指定key中存放set集合
        return Results.success("退出成功");
    }


//    public Results testService(){
//        throw new CustomizeException(CustomizeErrorCode.SYS_USER_NOFOUND);
//    }
}
