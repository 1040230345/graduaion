package com.jackson.model;

import lombok.Data;

/**
 * 课程模型
 * 2020年03月03日15:51:23
 */
@Data
public class Curriculum extends BaseEntity<Integer>{
    private String title;
    private String thumbnail;
    private String synopsis;
    private Integer userId;
}
