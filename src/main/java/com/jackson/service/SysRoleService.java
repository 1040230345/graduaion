package com.jackson.service;

import com.jackson.dao.RoleMapper;
import com.jackson.model.SysRole;
import com.jackson.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysRoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 获取角色信息
     * @param roleId
     * @return
     */
    public SysRole getRole(Integer roleId) {
        return roleMapper.getRole(roleId);
    }
}
