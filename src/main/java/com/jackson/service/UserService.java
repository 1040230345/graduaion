package com.jackson.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jackson.dto.SysUserDto;
import com.jackson.mapper.RoleMapper;
import com.jackson.mapper.UserCurrMapper;
import com.jackson.mapper.UserMapper;
import com.jackson.mapper.UserRoleMapper;
import com.jackson.exception.CustomizeErrorCode;
import com.jackson.exception.CustomizeException;
import com.jackson.model.SysRole;
import com.jackson.model.SysRoleUser;
import com.jackson.model.SysUser;
import com.jackson.myUtils.IpUtils;
import com.jackson.result.Results;
import com.jackson.security.utils.JwtTokenUtils;
import com.jackson.threadLocal.RequestHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户操作事务层
 */

@Service
@Transactional
public class UserService extends ServiceImpl<UserMapper, SysUser> {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCurrMapper userCurrMapper;

    /**
     * 获取用户
     * @param username
     * @return
     */
    public SysUser getUser(String username) {
        return userMapper.getUser(username);
    }

    /**
     * 用户注册
     * @return
     */
    public Results register(String username, String password, Integer userType, HttpServletRequest request) {

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
        sysRoleUser.setRoleId(userType);
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
        String userIp = IpUtils.getIpAddr(request);
        String token = JwtTokenUtils.createToken(username, roles, false,userIp);

        stringRedisTemplate.opsForValue().set(username,token,60*60, TimeUnit.SECONDS);

        return Results.success("注册成功",token);
    }

    /**
     * 用户退出
     * @param token
     * @return
     */
    public Results loginOut(String token) {
        stringRedisTemplate.opsForSet().add("BlackToken", token);//向指定key中存放set集合
        return Results.success("退出成功");
    }

    /**
     * 获取用户信息
     */
    public Results getUserMsg(String username){

        SysUser sysUser = userService.getOne(new QueryWrapper<SysUser>().eq("username",username));
        //获取用户角色
        String role = roleMapper.getUserRole(sysUser.getId());

        SysUserDto sysUserDto = new SysUserDto();
        BeanUtils.copyProperties(sysUser, sysUserDto);
        sysUserDto.setRole(role);
        //获取用户信息
        return Results.success(sysUserDto);
    }

    /**
     * 获取自己试验过得课程
     */
    public Results getUserCurr(String username){

        return  Results.success(userCurrMapper.getUserCurr(username));
    }

    /**
     * 删除自己做过的实验
     */
    public Results delUserCurr(Integer userCurrId){
        //获取用户username
        String username = (String) RequestHolder.getId();

        Integer delNum = userCurrMapper.delUserCurr(username,userCurrId);

        if(delNum>0){
            //根据key删除缓存
            stringRedisTemplate.delete(username+"=="+userCurrId);
            return Results.success();
        }
        return Results.failure();
    }

}
