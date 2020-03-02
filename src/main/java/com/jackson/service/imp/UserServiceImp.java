package com.jackson.service.imp;

import com.jackson.dao.UserDao;
import com.jackson.exception.CustomizeErrorCode;
import com.jackson.exception.CustomizeException;
import com.jackson.model.SysUser;
import com.jackson.result.Results;
import com.jackson.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public SysUser getUser(String username) {
        return userDao.getUser(username);
    }

    @Override
    public SysUser register(SysUser umsAdminParam) {
        return null;
    }

    @Override
    public String login(String username, String password) {
        return null;
    }


    public Results testService(){
        throw new CustomizeException(CustomizeErrorCode.SYS_USER_NOFOUND);
    }
}
