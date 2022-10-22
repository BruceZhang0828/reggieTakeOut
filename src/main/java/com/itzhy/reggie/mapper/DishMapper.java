package com.itzhy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itzhy.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhy
 * @description
 * @date 2022/10/22 14:24
 **/
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
