package com.jackson.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 课程章节模型
 */
@Data
public class Chapter extends BaseEntity {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private Integer curriculumId;

    private String title;

    private String text;

}
