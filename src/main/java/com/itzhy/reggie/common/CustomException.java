package com.itzhy.reggie.common;

/**
 * @author zhy
 * @description 自定义异常类
 * @date 2022/10/22 14:43
 **/
public class CustomException extends RuntimeException{
    public CustomException(String msg) {
        super(msg);
    }
}
