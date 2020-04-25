package com.jackson.service;

import com.jackson.exception.CustomizeErrorCode;
import com.jackson.exception.CustomizeException;
import com.jackson.myUtils.FileUtils;
import com.jackson.result.Results;
import com.jackson.service.FileUpService;
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

    private static final String IMAGE_PATH="images";

    @Autowired
    private FileUtils fileUtils;

    @Value("${image.ImagePath}")
    private String ImagePath;

    /**
     *
     * @param file 文件
     * @return
     */
    public Map upload(MultipartFile file, int type){

        Map map = new HashMap();
        if (file.isEmpty()) {

            map.put("success", 0);
            map.put("message", "获取不到上传文件");
            // 设置错误状态码
            return map;
        }
        // 拿到文件名
        String fileName = file.getOriginalFilename();

        //判断是哪种类型的图片
        String imagePath = getImagePath(type);

        //指定文件夹
        String path = ImagePath+imagePath;

        // 生成新的文件名
        String newName = fileUtils.getFileName(fileName);

        // 新的路径
        String filePath = path+"/"+newName;

        File dest = new File(filePath);
        //判断文件父目录是否存在
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }
        try {
            //保存文件
            file.transferTo(dest);
            map.put("success", 1);
            map.put("message", "上传成功");
            map.put("url", "http://localhost:8090"+"/"+IMAGE_PATH+imagePath+"/"+newName);
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
