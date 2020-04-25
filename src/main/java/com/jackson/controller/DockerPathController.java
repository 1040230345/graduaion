package com.jackson.controller;


import com.jackson.result.Results;
import com.jackson.service.DockerPathService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 实验类型
 */
@Controller
@ResponseBody
@RequestMapping("/docker")
@Api(tags = "实验类型获取")
public class DockerPathController {

    @Autowired
    private DockerPathService dockerPathService;

    @GetMapping("/dockerPath")
    @ApiOperation("获取实验类型")
    public Results getDockerPath(){
        return Results.success(dockerPathService.list());
    }
}
