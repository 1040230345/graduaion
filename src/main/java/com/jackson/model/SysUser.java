package com.jackson.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 用户模型
 */
@Data
public class SysUser extends BaseEntity<Integer>{
    private String username;
    private String password;
    private String nickname;
    private String headImgUrl;
    private String phone;
    private String email;
    private Integer sex;
    private Integer status;
    private String intro;

    public interface Status {
        int DISABLED = 0;
        int VALID = 1;
        int LOCKED = 2;
    }
}
