package com.jackson.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jackson.model.Chapter;
import com.jackson.result.Results;
import com.jackson.service.ChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Date;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/curriculum")
@Api(tags = "实验接口")
public class ChapterController {
    @Autowired
    private ChapterService chapterService;

    /**
     * 获取老师编辑的课程内容
     */
    @PostMapping("/upBookText")
    @ApiOperation("上传老师发送的文本内容")
    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    public Results upBookText(Chapter chapter){
        chapter.setCreateTime(new Date().getTime());
        chapterService.save(chapter);
//        request.getSession().setAttribute("text",bookText);
        return Results.success("发送成功");
    }

    /**
     * 获取课程内容
     */
    @GetMapping("/getBookText")
    @ApiOperation("获取课程示例文本")
    public Results getBookText(Integer currId){
        List<Chapter> chapterList =
                chapterService.list(new QueryWrapper<Chapter>().eq("curriculum_id",currId));
        //处理
        if(chapterList!=null){
            chapterList.forEach(e->{
                PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
                e.setText(pdp.markdownToHtml(e.getText()));
            });
        }
//        String text  = (String)request.getSession().getAttribute("text");
//        PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
//        String htmlContent = pdp.markdownToHtml(text);
////        System.out.println(htmlContent);
//        return Results.success(htmlContent);
        return Results.success(chapterList);
    }

    /**
     * 依靠课程id获取课程内容
     */
    @GetMapping("/getText")
    @ApiOperation("课程id获取课程内容")
    public Results getText(Integer classId){

        return chapterService.getText(classId);
    }
}
