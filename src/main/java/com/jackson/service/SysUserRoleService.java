package com.jackson.service;

import com.jackson.model.SysRoleUser;

import java.util.List;

public interface SysUserRoleService {
    /**
     * 获取用户角色列表
     */
    List<SysRoleUser> listByUserId(Integer userId);
}
