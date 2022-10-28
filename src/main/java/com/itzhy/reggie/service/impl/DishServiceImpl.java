package com.itzhy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.springframework.beans.BeanUtils;
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
    // 根据id查询菜品数据以及口味信息
    @Override
    public DishDto getByIdWithFlavor(long id) {
        // 查询菜品数据
        Dish dish = this.getById(id);
        // 转化数据
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        // 查询口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }

    // 更新菜品和关联口味
    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        // 更新菜品数据
        this.updateById(dishDto);
        // 删除之前的口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        Long dishDtoId = dishDto.getId();
        queryWrapper.eq(DishFlavor::getDishId, dishDtoId);
        dishFlavorService.remove(queryWrapper);
        // 将口味数据重新保存
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDtoId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
