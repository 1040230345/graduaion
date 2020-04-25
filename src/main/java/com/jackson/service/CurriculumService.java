package com.jackson.service;

import com.jackson.mapper.CurriculumMapper;
import com.jackson.result.Results;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CurriculumService {
    @Autowired
    private CurriculumMapper curriculumMapper;

    /**
     * 获取课程列表
     * @return
     */
    public Results getCurriculum(int startPosition) {
        //计算出应该是截取到哪里
        startPosition = (startPosition-1)*12;
        int limit = 12;
        //返回数据总数、本页信息
        return Results.success(curriculumMapper.countAllCurriculum().intValue(), curriculumMapper.getCurriculumList(startPosition,limit));
    }
}
