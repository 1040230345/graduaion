package com.jackson.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ApiController {

    /**
     * 统一页面返回
     * @param pageName
     * @return
     */
    @GetMapping(value="/getPage")
    public String getPage(@RequestParam(required = false,defaultValue = "index") String pageName){
        return pageName;
    }

    /**
     * 返回课程内容编辑页面
     */
    @GetMapping(value = "getMyMd")
    public String getMyMd(){
        return "myMarkDown";
    }

    /**
     * 获取主页面
     */
    @GetMapping("/index")
    public String getIndex(){
        return "index";
    }

    /**
     * 测试
     */
    @GetMapping("/m")
    public String getMIndex(){
        return "/m_index";
    }


}
