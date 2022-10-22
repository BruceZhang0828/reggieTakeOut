package com.itzhy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itzhy.reggie.entity.Category;
import com.itzhy.reggie.entity.Dish;
import com.itzhy.reggie.mapper.CategoryMapper;
import com.itzhy.reggie.mapper.DishMapper;
import com.itzhy.reggie.service.CategoryService;
import com.itzhy.reggie.service.DishService;
import org.springframework.stereotype.Service;

/**
 * @author zhy
 * @description 菜品
 * @date 2022/10/20 21:19
 **/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
