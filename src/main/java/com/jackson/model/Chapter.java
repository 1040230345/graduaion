package com.jackson.model;

import lombok.Data;

/**
 * 课程章节模型
 */
@Data
public class Chapter extends BaseEntity {
    private Integer id;
    private Integer curriculumId;
    private String title;
    private String dockerPath;
    private String text;

}
