package com.itzhy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itzhy.reggie.common.CustomException;
import com.itzhy.reggie.dto.SetmealDto;
import com.itzhy.reggie.entity.Category;
import com.itzhy.reggie.entity.Setmeal;
import com.itzhy.reggie.entity.SetmealDish;
import com.itzhy.reggie.mapper.SetmealMapper;
import com.itzhy.reggie.service.CategoryService;
import com.itzhy.reggie.service.SetmealDishService;
import com.itzhy.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private CategoryService categoryService;

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
     * @Param: [ids]
     * @return: void
     */
    @Override
    public void deleteWithDish(List<Long> ids) {
        // 判断是否有启售的
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
        if (count > 0) {
            // 售卖中的数据，不能删除
            throw new CustomException("套餐售卖，不能删除");
        }
        // 删除套餐数据
        this.removeByIds(ids);
        // 删除关联数据
        LambdaQueryWrapper<SetmealDish> sdQueryWrapper = new LambdaQueryWrapper<>();
        sdQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(sdQueryWrapper);
    }

    /**
     * @Description: 根据id查询数据和套餐菜品
     * @Author: zhy
     * @Date: 2022/10/29 11:11
     * @Param: [id]
     * @return: com.itzhy.reggie.dto.SetmealDto
     */
    @Override
    public SetmealDto getByIdWithDish(long id) {
        // 查询套餐
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        // 查询套餐菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishes);
        return setmealDto;
    }

    /**
     * @Description: 更新数据
     * @Author: zhy
     * @Date: 2022/10/29 11:15
     * @Param: [setmealDto]
     * @return: void
     */
    @Transactional
    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);
        // 之前的数据删除
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().forEach((item) -> {
            item.setSetmealId(setmealDto.getId());
        });
        // 新数据重新保存
        setmealDishService.saveBatch(setmealDishes);
    }
}
