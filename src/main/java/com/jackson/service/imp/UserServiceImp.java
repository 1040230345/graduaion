package com.jackson.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jackson.dao.RoleMapper;
import com.jackson.dao.UserMapper;
import com.jackson.dao.UserRoleMapper;
import com.jackson.exception.CustomizeErrorCode;
import com.jackson.exception.CustomizeException;
import com.jackson.model.SysRole;
import com.jackson.model.SysRoleUser;
import com.jackson.model.SysUser;
import com.jackson.result.Results;
import com.jackson.security.utils.JwtTokenUtils;
import com.jackson.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户操作事务层
 */

@Service
@Transactional
public class UserServiceImp implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 获取用户
     * @param username
     * @return
     */
    @Override
    public SysUser getUser(String username) {
        return userMapper.getUser(username);
    }

    /**
     * 用户注册
     * @return
     */
    @Override
    public Results register(String username,String password,Integer userType) {

        //数据校验
        if(username==null||password==null||userType==null){
            return Results.failure(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        //判断用户名是否存在
        SysUser sysUser = userMapper.getUser(username);

        if(sysUser!=null){
            return Results.failure(CustomizeErrorCode.SYS_USER_IS_HAVA);
        }

        //插入数据表
        sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setPassword(new BCryptPasswordEncoder().encode(password));
        sysUser.setHeadImageUrl("https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg");
        sysUser.setCreateTime(new Date().getTime());

        int num = userMapper.insert(sysUser);

        //赋予权限
        SysRoleUser sysRoleUser = new SysRoleUser();
        sysRoleUser.setRoleId(1);
        sysRoleUser.setUserId(sysUser.getId());
        sysRoleUser.setCreateTime(new Date().getTime());
        userRoleMapper.insert(sysRoleUser);

        //生成token返回
        List<String> roles = new ArrayList<>();
        roleMapper.selectList(new QueryWrapper<SysRole>().eq("id",userType)).forEach(e->{
            roles.add(e.getName());
        });

        if(roles.size()<1||num<1){
            throw  new CustomizeException(CustomizeErrorCode.SYS_ERROR);
        }

        // 创建 Token
        String token = JwtTokenUtils.createToken(username, roles, false);

        return Results.success("注册成功",token);
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

}
