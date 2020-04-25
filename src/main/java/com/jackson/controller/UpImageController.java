package com.jackson.controller;


import com.jackson.result.Results;
import com.jackson.service.FileUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

/**
 * 用于上传文件
 */
@Controller
@RequestMapping("/image")
@ResponseBody
public class UpImageController {
    @Autowired
    private FileUpService fileUpService;

    /**
     * 上传文件
     */
    @PostMapping("/upImage")
    public Results upImage(@RequestParam(value = "editormd-image-file") MultipartFile userImage, @RequestParam(defaultValue = "2") Integer type){
        return fileUpService.upload(userImage,type);
    }

//    /**
//     * 下载文件
//     */
//    /**
//     * 图片下载
//     * @param uuid
//     * @param request
//     * @param response
//     */
//    @RequestMapping(value = "/download")
//    @ResponseBody
//    private void download(String uuid, HttpServletRequest request, HttpServletResponse response) {
//        fileService.download(uuid, request, response);
//    }

}
