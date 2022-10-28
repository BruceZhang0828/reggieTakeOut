package com.itzhy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itzhy.reggie.common.R;
import com.itzhy.reggie.dto.DishDto;
import com.itzhy.reggie.entity.Category;
import com.itzhy.reggie.entity.Dish;
import com.itzhy.reggie.service.CategoryService;
import com.itzhy.reggie.service.DishFlavorService;
import com.itzhy.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhy
 * @description
 * @date 2022/10/25 22:57
 **/
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("菜品保存成功。");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 分页查询
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        // 查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        // 设置名称匹配
        queryWrapper.like(name != null, Dish::getName, name);
        // 设置排序
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        // 查询
        dishService.page(pageInfo, queryWrapper);
        // 复制分页信息
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        // 将dish转为dishDto
        List<Dish> dishList = pageInfo.getRecords();
        List<DishDto> dishDtoList = dishList.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            // 查询分类信息
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());
        // 将处理完的disDto设置到分页信息中
        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }

    /**
     * @Description: 根据id查询菜品记录详情
     * @Author: zhy
     * @Date: 2022/10/28 10:05
     * @Param: [id]
     * @return: com.itzhy.reggie.common.R<com.itzhy.reggie.dto.DishDto>
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * @Description: 修改菜品数据
     * @Author: zhy
     * @Date: 2022/10/28 10:05
     * @Param: [dishDto]
     * @return: com.itzhy.reggie.common.R<java.lang.String>
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }

    /**
     * @Description: 起售和停售
     * @Author: zhy
     * @Date: 2022/10/28 10:05
     * @Param: [status, ids]
     * @return: com.itzhy.reggie.common.R<java.lang.String>
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable int status, long ids) {
        Dish dish = new Dish();
        dish.setId(ids);
        dish.setStatus(status);
        // 更新数据
        dishService.updateById(dish);
        return R.success("菜品数据更新成功");
    }
    /**
     * @Description: 删除
     * @Author: zhy
     * @Date: 2022/10/28 10:18
     * @Param:  [ids]
     * @return: com.itzhy.reggie.common.R<java.lang.String>
     */
    @DeleteMapping
    public R<String> delete(String ids) {
        dishService.removeById(ids);
        return R.success("删除成功");
    }
}
