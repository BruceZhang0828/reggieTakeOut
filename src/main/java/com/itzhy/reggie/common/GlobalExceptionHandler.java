package com.itzhy.reggie.common;

import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author zhy
 * @description 全局异常处理类
 * @date 2022/10/17 20:55
 **/

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * @Description: 异常处理方法
     * @Author: zhy
     * @Date: 2022/10/17 21:01
     * @Param:  [exception]
     * @return: com.itzhy.reggie.common.R<java.lang.String>
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        log.info(exception.getMessage());
        String exceptionMsg = exception.getMessage();
        if (exceptionMsg.contains("Duplicate entry")){
            String[] split = exceptionMsg.split(" ");
            return R.error(split[2]+"已存在！");
        }
        return R.error("未知错误！");
    }
}
