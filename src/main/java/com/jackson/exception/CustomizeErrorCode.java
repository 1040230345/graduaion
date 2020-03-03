package com.jackson.exception;

/**
 * 2020年03月03日01:50:33
 * jackson
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode {
    // 公共请求信息
    SUCCESS(200, "请求成功"),
    TABLE_SUCCESS(200, "请求成功"),
    FAIL(500, "请求失败"),
    PARAMETER_MISSING(600,"参数缺失"),
    UNAUTHORIZED(401,"未授权"),

    QUESTION_NOT_FOUND(2001, "你找到问题不在了，要不要换个试试？"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复"),
    NO_LOGIN(2003, "当前操作需要登录，请登陆后重试"),
    SYS_ERROR(2004, "服务冒烟了，要不然你稍后再试试！！！"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "回复的评论不存在了，要不要换个试试？"),
    CONTENT_IS_EMPTY(2007, "输入内容不能为空"),
    READ_NOTIFICATION_FAIL(2008, "兄弟你这是读别人的信息呢？"),
    NOTIFICATION_NOT_FOUND(2009, "消息莫非是不翼而飞了？"),
    FILE_UPLOAD_FAIL(2010, "图片上传失败"),
    INVALID_INPUT(2011, "非法输入"),
    INVALID_OPERATION(2012, "兄弟，是不是走错房间了？"),
    SYS_NO_USER(2013,"用户名不存在"),
    SYS_USER_LOCK(2014,"用户被锁定,请联系管理员"),
    SYS_USER_DELETE(2015,"用户已作废"),
    SYS_USER_NOFOUND(2016,"账号密码错误")

    ;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
