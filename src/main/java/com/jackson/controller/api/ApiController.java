package com.jackson.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
//        if(pageName.equals("index")){
//            return pageName;
//        }
//        modelAndView.setViewName(pageName);
//        System.out.println(pageName);
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

}
