package com.jackson.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Experiment implements Serializable {
    /**
     * 容器名称
     * 镜像名称
     */
    private String dockerName;
    private String imageName;
}
