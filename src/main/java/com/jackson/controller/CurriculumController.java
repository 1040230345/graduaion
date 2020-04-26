package com.jackson.controller;


import com.jackson.mapper.UserMapper;
import com.jackson.model.Curriculum;
import com.jackson.model.SysUser;
import com.jackson.result.Results;
import com.jackson.service.CurriculumService;
import com.jackson.service.UserService;
import com.jackson.threadLocal.RequestHolder;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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
    @Autowired
    private UserService userService;

    /**
     * 分页获取所以实验列表
     */
    @GetMapping("/getCurriculumList")
    @ApiOperation("分页获取课程列表")
    public Results<Curriculum> getCurriculumList(int startPosition ){
        return curriculumService.getCurriculum(startPosition);
    }

    /**
     * 获取个人的实验
     */
    @GetMapping("/userCurriculumList")
    @ApiOperation("获取个人实验列表")
    public Results getUserCurriculumList(){
        String username = (String) RequestHolder.getId();
        return curriculumService.getUserCurriculum(username);
    }

    /**
     * 获取老师编辑的课程内容
     */
    @PostMapping("/upBookText")
    @ApiOperation("获取老师发送的文本内容")
//    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    public Results upBookText(String bookText, HttpServletRequest request){
        request.getSession().setAttribute("text",bookText);
        return Results.success("发送成功");
    }

    /**
     * 获取课程内容
     */
    @GetMapping("/getBookText")
    @ApiOperation("获取课程示例文本")
    public Results getBookText(HttpServletRequest request){

//        String text  = (String)request.getSession().getAttribute("text");
//        PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
//        String htmlContent = pdp.markdownToHtml(text);
////        System.out.println(htmlContent);
//        return Results.success(htmlContent);
        return null;
    }

    /**
     * 删除实验
     */
    @DeleteMapping("/Curriculum")
    @ApiOperation("删除实验")
    public Results delCurriculum(Integer currId){
        if (curriculumService.removeById(currId)) {
            return Results.success();
        }
        return Results.failure();
    }

    /**
     * 添加实验
     */
    @PostMapping("/Curriculum")
    @ApiOperation("添加实验")
    public Results addCurriculum( Curriculum curriculum){
        String username = (String) RequestHolder.getId();
        //获取用户id
        SysUser sysUser = userService.getUser(username);

        curriculum.setUserId(sysUser.getId());
        curriculum.setCreateTime(new Date().getTime());

        if (curriculumService.save(curriculum)) {
            return Results.success();
        }
        return Results.failure();
    }

}
