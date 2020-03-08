package com.jackson.webSocket.service;

public interface WebSocketService {

    /**
     * 初始化容器
     */
    String initContainer(Integer chapterId,String token);
}
