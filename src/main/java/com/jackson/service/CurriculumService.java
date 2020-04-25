package com.jackson.service;

import com.jackson.dao.CurriculumDao;
import com.jackson.result.Results;
import com.jackson.service.CurriculumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CurriculumService {
    @Autowired
    private CurriculumDao curriculumDao;

    /**
     * 获取课程列表
     * @return
     */
    public Results getCurriculum(int startPosition) {
        //计算出应该是截取到哪里
        startPosition = (startPosition-1)*12;
        int limit = 12;
        //返回数据总数、本页信息
        return Results.success(curriculumDao.countAllCurriculum().intValue(),curriculumDao.getCurriculumList(startPosition,limit));
    }
}
