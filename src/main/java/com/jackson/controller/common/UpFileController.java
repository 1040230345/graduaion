package com.jackson.controller.common;

import com.jackson.result.Results;
import com.jackson.service.FileUpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/common/upFile")
@ResponseBody
@Api(tags = "通用类接口-->上传文件")
@CrossOrigin(origins = "*",maxAge = 3600)
@Slf4j
public class UpFileController {

    @Autowired
    private FileUpService fileUpService;

    @ApiOperation("上传图片")
    @PostMapping("/image/{imageType}")
    public Results upSchoolImage(@RequestParam("fileName") MultipartFile image,
                                 @PathVariable("imageType") Integer imageType){
        return fileUpService.upload(image,imageType);
    }

    /**
     * 上传文件
     */
    @PostMapping("/upImage")
    public Map upImage(@RequestParam(value = "editormd-image-file") MultipartFile userImage, @RequestParam(defaultValue = "3") Integer type){

        return fileUpService.upload1(userImage,type);
    }
}
