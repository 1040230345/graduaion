package com.jackson.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jackson.mapper.DockerPathMapper;
import com.jackson.model.DockerPath;
import org.springframework.stereotype.Service;

@Service
public class DockerPathService extends ServiceImpl<DockerPathMapper, DockerPath> {
}
