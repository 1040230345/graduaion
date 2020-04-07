package com.jackson.security.service;

import com.jackson.model.SysRole;
import com.jackson.model.SysRoleUser;
import com.jackson.model.SysUser;
import com.jackson.service.SysRoleService;
import com.jackson.service.SysUserRoleService;
import com.jackson.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author jackson
 * 2020年03月02日16:24:58
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleService sysRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        SysUser sysUser = userService.getUser(username);

        if (sysUser == null) {
            throw new AuthenticationCredentialsNotFoundException("用户名不存在");
        } else if (sysUser.getStatus() == SysUser.Status.LOCKED) {
            throw new LockedException("用户被锁定,请联系管理员");
        } else if (sysUser.getStatus() == SysUser.Status.DISABLED) {
            throw new DisabledException("用户已作废");
        }

        // 添加权限
        List<SysRoleUser> userRoles = sysUserRoleService.listByUserId(sysUser.getId());
        for (SysRoleUser userRole : userRoles) {
            SysRole role = sysRoleService.getRole(userRole.getRoleId());
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        // 返回UserDetails实现类
        return new User(sysUser.getUsername(), sysUser.getPassword(), authorities);
    }


}
