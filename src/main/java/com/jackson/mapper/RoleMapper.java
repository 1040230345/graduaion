package com.jackson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jackson.model.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoleMapper extends BaseMapper<SysRole> {

    @Select("select * from sys_role where id = #{roleId} ")
    SysRole getRole(@Param("roleId") Integer roleId);

    @Select("select sr.name from sys_role sr " +
            "join sys_role_user sru on sr.id = sru.role_id " +
            "where sru.user_id = #{userId} ")
    String getUserRole(@Param("userId") Integer userId);
}
