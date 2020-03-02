package com.jackson.dao;

import com.jackson.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {
    /**
     * 查找用户依靠用户名
     */
    @Select("select * from sys_user where username = #{username} ")
    SysUser getUser(@Param("username") String username);
}
