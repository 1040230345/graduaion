package com.jackson.dao;

import com.jackson.model.SysRoleUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRoleDao {
    /**
     * 依靠用户id查找他的角色
     */
    @Select("SELECT * FROM sys_role_user WHERE userId = #{userId}")
    List<SysRoleUser> listByUserId(Integer userId);
}
