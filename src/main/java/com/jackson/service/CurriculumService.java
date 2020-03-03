package com.jackson.service;

import com.jackson.result.Results;


/**
 * 课程接口
 */
public interface CurriculumService {
    /**
     * 获取课程列表
     */
    Results getCurriculum(int startPosition);
}
