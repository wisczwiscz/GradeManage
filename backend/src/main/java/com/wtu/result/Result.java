package com.wtu.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果
 * @param <T> 数据类型
 */
@Data
public class Result<T> implements Serializable {

    /**
     * 状态码
     */
    private Integer code;
    
    /**
     * 提示信息
     */
    private String message;
    
    /**
     * 数据
     */
    private T data;
    
    /**
     * 成功结果
     * @param data 数据
     * @param <T> 数据类型
     * @return 返回成功结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 成功结果（无数据）
     * @return 返回成功结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }
    
    /**
     * 失败结果
     * @param message 错误信息
     * @return 返回失败结果
     */
    public static <T> Result<T> fail(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败结果（自定义状态码）
     * @param code 状态码
     * @param message 错误信息
     * @return 返回失败结果
     */
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

}
