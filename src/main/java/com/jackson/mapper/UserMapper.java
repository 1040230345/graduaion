package com.jackson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jackson.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
    /**
     * 查找用户依靠用户名
     */
    @Select("select * from sys_user where username = #{username} ")
    SysUser getUser(@Param("username") String username);
}
