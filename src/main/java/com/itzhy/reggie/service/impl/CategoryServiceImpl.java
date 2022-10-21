package com.itzhy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itzhy.reggie.entity.Category;
import com.itzhy.reggie.mapper.CategoryMapper;
import com.itzhy.reggie.service.CategoryService;
import org.springframework.stereotype.Service;

/**
 * @author zhy
 * @description 分类业务处理实现类
 * @date 2022/10/20 21:19
 **/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
