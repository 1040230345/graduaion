package com.jackson.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_curr")
public class UserCurr extends BaseEntity{
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String userName;

    private Integer currId;

    private String currName;


}
