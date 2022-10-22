package com.itzhy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itzhy.reggie.common.CustomException;
import com.itzhy.reggie.common.R;
import com.itzhy.reggie.entity.Category;
import com.itzhy.reggie.entity.Dish;
import com.itzhy.reggie.entity.Setmeal;
import com.itzhy.reggie.mapper.CategoryMapper;
import com.itzhy.reggie.service.CategoryService;
import com.itzhy.reggie.service.DishService;
import com.itzhy.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhy
 * @description 分类业务处理实现类
 * @date 2022/10/20 21:19
 **/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void deleteById(Long id) {
        LambdaQueryWrapper<Dish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(queryWrapper1);
        if (count1 > 0) {
            // 分类关联了相关的菜品
            throw new CustomException("该分类已经关联相关菜品");
        }
        LambdaQueryWrapper<Setmeal> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(queryWrapper2);
        if (count2 > 0) {
            // 分类关联了相关的套餐
            throw new CustomException("该分类已经关联相关套餐");
        }
        // 正常的删除
        super.removeById(id);
    }
}
