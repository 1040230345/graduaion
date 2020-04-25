package com.jackson.service;

import com.jackson.mapper.UserRoleMapper;
import com.jackson.model.SysRoleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserRoleService {
    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 获取用户角色列表
     * @param userId
     * @return
     */
    public List<SysRoleUser> listByUserId(Integer userId) {
        return userRoleMapper.listByUserId(userId);
    }

}
