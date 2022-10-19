package com.itzhy.reggie.common;

/**
 * @author zhy
 * @description threadLocal工具类
 * @date 2022/10/19 21:54
 **/
public class BaseContent {

    public static ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<Long>();
    // 设置id
    public static void setCurId(Long id) {
        THREAD_LOCAL.set(id);
    }
    // 获取用id
    public static Long getCurId() {
        return THREAD_LOCAL.get();
    }
}
