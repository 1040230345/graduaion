package com.jackson.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 用户模型
 */
@Data
public class SysUser extends BaseEntity{

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String headImageUrl;
    private String phone;
    private String email;
    private Integer sex;
    private Integer status;

    public interface Status {
        int DISABLED = 0;
        int VALID = 1;
        int LOCKED = 2;
    }
}
