package com.itzhy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itzhy.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhy
 * @description 分类Mapper
 * @date 2022/10/20 21:16
 **/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
