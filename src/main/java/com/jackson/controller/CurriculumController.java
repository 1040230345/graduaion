package com.jackson.controller;


import com.jackson.model.Curriculum;
import com.jackson.result.Results;
import com.jackson.service.CurriculumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * 获取老师编辑的课程内容
     */
    @PostMapping("/upBookText")
    @ApiOperation("获取老师发送的文本内容")
    public Results upBookText(String bookText, HttpServletRequest request){
        request.setAttribute("text",bookText);
        System.out.println(bookText);
        return Results.success("发送成功");
    }

    /**
     * 获取课程内容
     */
    @GetMapping("/getBookText")
    public Results getBookText(HttpServletRequest request){
        System.out.println("进来了吗啊");
        String text  = (String)request.getSession().getAttribute("text");
        return Results.success(text);
    }

}
