package com.jackson.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role_user")
public class SysRoleUser extends BaseEntity{
    private Integer userId;
    private Integer roleId;
}
