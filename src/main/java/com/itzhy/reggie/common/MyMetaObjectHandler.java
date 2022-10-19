package com.itzhy.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author zhy
 * @description 公共字段处理类
 * @date 2022/10/19 21:37
 **/
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * @Description: 插入数据，自动填充
     * @Author: zhy
     * @Date: 2022/10/19 21:46
     * @Param:  [metaObject]
     * @return: void
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充【insert】...");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser",BaseContent.getCurId());
        metaObject.setValue("updateUser",BaseContent.getCurId());
    }
    /**
     * @Description: 更新数据，自动填充
     * @Author: zhy
     * @Date: 2022/10/19 21:46
     * @Param:  [metaObject]
     * @return: void
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充【update】...");
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContent.getCurId());
    }
}
