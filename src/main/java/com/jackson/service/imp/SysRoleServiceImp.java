package com.jackson.service.imp;

import com.jackson.dao.RoleDao;
import com.jackson.model.SysRole;
import com.jackson.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImp implements SysRoleService {

    @Autowired
    private RoleDao roleDao;

    /**
     * 获取角色信息
     * @param roleId
     * @return
     */
    @Override
    public SysRole getRole(Integer roleId) {
        return roleDao.getRole(roleId);
    }
}
