package com.jackson.dao;

import com.jackson.model.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoleDao {

    @Select("select * from sys_role where id = #{roleId} ")
    SysRole getRole(@Param("roleId") Integer roleId);
}
