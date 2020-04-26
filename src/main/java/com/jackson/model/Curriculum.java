package com.jackson.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 课程模型
 * 2020年03月03日15:51:23
 */
@Data
@TableName("curriculum")
public class Curriculum extends BaseEntity{
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String title;

    private String thumbnail;

    private String synopsis;

    private Integer userId;

    private String dockerPath;

}
