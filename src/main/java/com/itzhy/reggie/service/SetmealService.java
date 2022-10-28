package com.itzhy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itzhy.reggie.dto.SetmealDto;
import com.itzhy.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author zhy
 * @description
 * @date 2022/10/22 14:27
 **/
public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    void deleteWithDish(List<Long> ids);
}
