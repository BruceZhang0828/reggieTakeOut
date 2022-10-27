package com.itzhy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itzhy.reggie.dto.DishDto;
import com.itzhy.reggie.entity.Dish;

/**
 * @author zhy
 * @description
 * @date 2022/10/22 14:26
 **/
public interface DishService extends IService<Dish> {
    // 保存数据
    void saveWithFlavor(DishDto dishDto);
}
