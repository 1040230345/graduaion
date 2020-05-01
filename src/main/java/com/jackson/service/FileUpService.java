package com.jackson.service;

import com.jackson.exception.CustomizeErrorCode;
import com.jackson.myUtils.FileUtils;
import com.jackson.result.Results;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 上传事务层
 */
@Service
@Slf4j
@Transactional
public class FileUpService  {

    private static final String IMAGE_URL_PATH="/images";

    @Value("${image.ImagePath}")
    private String imagePath;

    @Autowired
    private FileUtils fileUtils;

    /**
     *
     * @param file 文件
     * @return
     */
    public Results upload(MultipartFile file, Integer type){

        if(type==null||type.equals("")){
            log.warn("up file is error type is null ");
            return Results.failure(CustomizeErrorCode.UPFILE_NO_Find);
        }

        if (file.isEmpty()) {
            // 设置错误状态码
            return Results.failure();
        }
        // 拿到文件名
        String fileName = file.getOriginalFilename();

        //获取图片类型地址
        String imageTypePath = getImagePath(type);

        //指定文件夹
        String path = imagePath+imageTypePath;

        // 生成新的文件名
        String newName = fileUtils.getFileName(fileName);

        // 新的路径
        String filePath = path+"/"+newName;

        File dest = new File(new File(filePath).getAbsolutePath());
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            return Results.success(IMAGE_URL_PATH+imageTypePath+"/"+newName);
        } catch (IllegalStateException e) {
            log.error("upload file failed，",e);
            return Results.failure();
        } catch (IOException e) {
            log.error("Read file error,",e);
            return Results.failure(CustomizeErrorCode.FILE_READ_ERROR);
        }

    }

    /**
     *
     * @param file 文件
     * @return
     */
    public Map upload1(MultipartFile file, Integer type){

        Map map = new HashMap();
        if (file.isEmpty()) {

            map.put("success", 0);
            map.put("message", "获取不到上传文件");
            // 设置错误状态码
            return map;
        }
        // 拿到文件名
        String fileName = file.getOriginalFilename();

        //获取图片类型地址
        String imageTypePath = getImagePath(type);

        //指定文件夹
        String path = imagePath+imageTypePath;

        // 生成新的文件名
        String newName = fileUtils.getFileName(fileName);

        // 新的路径
        String filePath = path+"/"+newName;

        File dest = new File(new File(filePath).getAbsolutePath());

        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }

        try {
            //保存文件
            file.transferTo(dest);
            map.put("success", 1);
            map.put("message", "上传成功");
            map.put("url", "http://localhost:8090"+IMAGE_URL_PATH+imageTypePath+"/"+newName);
            //返回文件名
//            return Results.success("/"+IMAGE_PATH+"/"+imagePath+"/"+newName);
            return map;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            map.put("success", 0);
            map.put("message", "失败");
            log.error("环境问题"+e);
//            throw new CustomizeException(CustomizeErrorCode.SYS_ERROR);
            return map;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            map.put("success", 0);
            map.put("message", "失败");
            log.error("读写文件出错"+e);
//            throw new CustomizeException(CustomizeErrorCode.SYS_ERROR);
            return map;
        }

    }


    private String getImagePath(int type) {
        switch (type) {
            case 1:
                return "/userImage";
            case 2:
                return "/bookImage";
            case 3:
                return "/thumbnailImage";
            default:
                return "/unkonwType";
        }
    }
}
