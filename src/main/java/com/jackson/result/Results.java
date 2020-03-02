package com.jackson.result;

import com.jackson.exception.CustomizeErrorCode;
import com.jackson.exception.CustomizeException;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Results<T>  implements Serializable {

    int count;//数据数量
    Integer code;//代码
    String msg;//信息
    List<T> datas;//返回数据
    T data;//任何类型条件

    public Results() {
    }
    public Results(Integer code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }


    public Results(Integer code, String msg, T data, List<T> datas) {
        this.code = code;
        this.msg = msg;
        this.data = data;
//        this.count = count;
        this.datas = datas;
    }


    /* 无数据传输的 成功返回 */
    public static <T> Results<T> success() {
        return new Results<T>( CustomizeErrorCode.SUCCESS.getCode(),  CustomizeErrorCode.SUCCESS.getMessage());
    }

    public static <T> Results<T> success(String msg) {
        return new Results<T>(CustomizeErrorCode.SUCCESS.getCode(), msg);
    }

    public static <T> Results<T> success(CustomizeErrorCode resultCode) {
        return new Results<T>( resultCode.getCode(),  resultCode.getMessage());
    }

    /* 单个数据传输的 成功返回 */
    public static <T> Results<T> success(T data) {
        return new Results<T>( CustomizeErrorCode.SUCCESS.getCode(),  CustomizeErrorCode.SUCCESS.getMessage(), data,  null);
    }

    public static <T> Results<T> success(String msg, T data) {
        return new Results<T>(CustomizeErrorCode.SUCCESS.getCode(), msg, data,  null);
    }

    public static <T> Results<T> success(CustomizeErrorCode resultCode, T data) {
        return new Results<T>( resultCode.getCode(),  resultCode.getMessage(), data, null);
    }

    /* 分页数据传输的 成功返回 */
    public static <T> Results<T> success(Integer count, List<T> datas) {
        return new Results<T>(CustomizeErrorCode.TABLE_SUCCESS.getCode(),CustomizeErrorCode.SUCCESS.getMessage(),null, datas);
    }

    public static <T> Results<T> success(String msg, Integer count, List<T> datas) {
        return new Results<T>(CustomizeErrorCode.TABLE_SUCCESS.getCode(), msg, null,  datas);
    }

    public static <T> Results<T> success(CustomizeErrorCode resultCode, Integer count, List<T> datas) {
        return new Results<T>( resultCode.getCode(),  resultCode.getMessage(), null, datas);
    }
    /* 无数据传输的 失败返回 */
    public static <T> Results<T> failure() {
        return new Results<T>( CustomizeErrorCode.FAIL.getCode(), CustomizeErrorCode.FAIL.getMessage());
    }

    public static <T> Results<T> failure(CustomizeErrorCode resultCode) {
        return new Results<T>( resultCode.getCode(),  resultCode.getMessage());
    }

    public static <T> Results<T> failure(CustomizeException e) {
        return failure(e.getCode(), e.getMessage());
    }

    public static <T> Results<T> failure(Integer code, String msg) {
        return new Results<T>( code,  msg);
    }

    public static Results ok() {
        return new Results();
    }

}
