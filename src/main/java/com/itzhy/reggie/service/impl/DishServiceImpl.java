package com.itzhy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itzhy.reggie.dto.DishDto;
import com.itzhy.reggie.entity.Category;
import com.itzhy.reggie.entity.Dish;
import com.itzhy.reggie.entity.DishFlavor;
import com.itzhy.reggie.mapper.CategoryMapper;
import com.itzhy.reggie.mapper.DishMapper;
import com.itzhy.reggie.service.CategoryService;
import com.itzhy.reggie.service.DishFlavorService;
import com.itzhy.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhy
 * @description 菜品
 * @date 2022/10/20 21:19
 **/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    // 新增菜品同时，保存对应的口味数据
    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        // 保存菜品数据
        this.save(dishDto);
        // 获取保存菜品数据id
        Long dishId = dishDto.getId();
        // 菜品数据设置id
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        // 批量存储菜品口味
        dishFlavorService.saveBatch(flavors);
    }
}
