package com.jackson.controller;


import com.jackson.result.Results;
import com.jackson.security.utils.JwtTokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@ResponseBody
@RequestMapping("/experiment")
@Api(tags = "实验接口")
public class ExperimentController {
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("获取实验剩余时间")
    @PutMapping("/getTime")
    public Results getTime(HttpServletRequest request){
        String username = JwtTokenUtils.getUsernameByRequest(request);
        return null;
    }

//    @ApiOperation("延长实验")
//    @PutMapping("/extend")
//    public Results extend(){
//        return null;
//    }
//    @ApiOperation("删除实验")
//    @PutMapping("/extend")
//    public Results extend(){
//        return null;
//    }
//    @ApiOperation("保存实验")
//    @PutMapping("/extend")
//    public Results extend(){
//        return null;
//    }
}
