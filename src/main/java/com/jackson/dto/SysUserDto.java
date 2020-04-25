package com.jackson.dto;

import com.jackson.model.SysUser;
import lombok.Data;

@Data
public class SysUserDto extends SysUser {
    private String role;
}
