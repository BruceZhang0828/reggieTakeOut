package com.itzhy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itzhy.reggie.common.R;
import com.itzhy.reggie.entity.Category;
import com.itzhy.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhy
 * @description 分类业务处理类
 * @date 2022/10/20 21:21
 **/
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    // 存储分类数据
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("保存成功");
    }

    // 分页查询分类数据
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        // 构建分页器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        // 查询器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getSort);
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * @Description: 删除分类
     * @Author: zhy
     * @Date: 2022/10/21 22:50
     * @Param: [ids]
     * @return: com.itzhy.reggie.common.R<java.lang.String>
     */
    @DeleteMapping
    public R<String> deleteById(Long ids) {
        log.info("删除的分类id为：{}", ids);
        categoryService.deleteById(ids);
        return R.success("删除分类成功。");
    }


    /**
     * @Description: 修改分类
     * @Author: zhy
     * @Date: 2022/10/22 15:10
     * @Param: [category]
     * @return: com.itzhy.reggie.common.R<java.lang.String>
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("修改分类id为：{}", category.getId());
        categoryService.updateById(category);
        return R.success("修改分类成功");
    }

    @GetMapping("/list")
    public R<List> list(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        Integer type = category.getType();
        queryWrapper.eq(type!=null,Category::getType, type);
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
