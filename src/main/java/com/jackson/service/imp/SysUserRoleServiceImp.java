package com.jackson.service.imp;

import com.jackson.dao.UserRoleDao;
import com.jackson.model.SysRoleUser;
import com.jackson.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserRoleServiceImp implements SysUserRoleService {
    @Autowired
    private UserRoleDao userRoleDao;

    /**
     * 获取用户角色列表
     * @param userId
     * @return
     */
    @Override
    public List<SysRoleUser> listByUserId(Integer userId) {
        return userRoleDao.listByUserId(userId);
    }

}
