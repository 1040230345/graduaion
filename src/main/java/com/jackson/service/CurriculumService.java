package com.jackson.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jackson.mapper.CurriculumMapper;
import com.jackson.mapper.UserMapper;
import com.jackson.model.Curriculum;
import com.jackson.model.SysUser;
import com.jackson.result.Results;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class CurriculumService extends ServiceImpl<CurriculumMapper,Curriculum> {
    @Autowired
    private CurriculumMapper curriculumMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 获取所有实验列表
     * @return
     */
    public Results getCurriculum(int startPosition) {
        //计算出应该是截取到哪里
        startPosition = (startPosition-1)*12;
        int limit = 12;
        //返回数据总数、本页信息
        return Results.success(curriculumMapper.countAllCurriculum().intValue(), curriculumMapper.getCurriculumList(startPosition,limit));
    }

    /**
     * 获取个人实验
     */
    public Results getUserCurriculum(String username) {
        //获取个人信息
        SysUser sysUser = userMapper.getUser(username);

        if(sysUser!=null){
            //获取个人实验课程
            List<Curriculum> curriculumList = curriculumMapper.getUserCurriculum(sysUser.getId());
            return Results.success(curriculumList);
        }
        return Results.failure();
    }
}
