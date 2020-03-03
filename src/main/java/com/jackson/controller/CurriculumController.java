package com.jackson.controller;


import com.jackson.model.Curriculum;
import com.jackson.result.Results;
import com.jackson.service.CurriculumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 课程控制器
 */
@Controller
@ResponseBody
@RequestMapping("/curriculum")
@Api(tags = "课程接口")
public class CurriculumController {
    @Autowired
    private CurriculumService curriculumService;

    /**
     * 分页获取课程列表
     */
    @GetMapping("/getCurriculumList")
    @ApiOperation("分页获取课程列表")
    public Results<Curriculum> getCurriculumList(int startPosition ){
        return curriculumService.getCurriculum(startPosition);
    }
}
