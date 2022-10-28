package com.itzhy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itzhy.reggie.common.CustomException;
import com.itzhy.reggie.dto.SetmealDto;
import com.itzhy.reggie.entity.Setmeal;
import com.itzhy.reggie.entity.SetmealDish;
import com.itzhy.reggie.mapper.SetmealMapper;
import com.itzhy.reggie.service.SetmealDishService;
import com.itzhy.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhy
 * @description
 * @date 2022/10/22 14:29
 **/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().forEach((item) -> {
            item.setSetmealId(setmealDto.getId());
        });
        setmealDishService.saveBatch(setmealDishes);
    }
    /**
     * @Description:
     * @Author: zhy
     * @Date: 2022/10/28 23:11
     * @Param:  [ids]
     * @return: void
     */
    @Override
    public void deleteWithDish(List<Long> ids) {
        // 判断是否有启售的
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        if (count > 0) {
            // 售卖中的数据，不能删除
            throw new CustomException("套餐售卖，不能删除");
        }
        // 删除套餐数据
        this.removeByIds(ids);
        // 删除关联数据
        LambdaQueryWrapper<SetmealDish> sdQueryWrapper = new LambdaQueryWrapper<>();
        sdQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(sdQueryWrapper);
    }
}
