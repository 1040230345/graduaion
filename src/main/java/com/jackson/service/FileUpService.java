package com.jackson.service;

import com.jackson.result.Results;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 上传图片事务接口
 * jackson
 * 2020年03月05日00:10:59
 */
public interface FileUpService {
    /**
     * 图片上传
     */
    public Map upload(MultipartFile file, int type);
}
