package com.jackson.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jackson.model.SysRoleUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<SysRoleUser> {
    /**
     * 依靠用户id查找他的角色
     */
    @Select("SELECT * FROM sys_role_user WHERE user_id = #{userId}")
    List<SysRoleUser> listByUserId(Integer userId);
}
