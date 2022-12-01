package com.itzhy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itzhy.reggie.common.R;
import com.itzhy.reggie.dto.DishDto;
import com.itzhy.reggie.entity.Category;
import com.itzhy.reggie.entity.Dish;
import com.itzhy.reggie.entity.DishFlavor;
import com.itzhy.reggie.service.CategoryService;
import com.itzhy.reggie.service.DishFlavorService;
import com.itzhy.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        // 清理全部菜品缓存数据
       /* Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);*/
        // 清理单个分类的下的菜品缓存
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
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
        // 清理全部菜品缓存数据
       /* Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);*/
        // 清理单个分类的下的菜品缓存
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
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
     * @Param: [ids]
     * @return: com.itzhy.reggie.common.R<java.lang.String>
     */
    @DeleteMapping
    public R<String> delete(String ids) {
        dishService.removeById(ids);
        return R.success("删除成功");
    }

    /**
     * @Description: 根据条件查询菜品数据
     * @Author: zhy
     * @Date: 2022/10/28 15:35
     * @Param: [dish]
     * @return: com.itzhy.reggie.common.R<java.util.List < com.itzhy.reggie.entity.Dish>>
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        // 通过redis缓存查询
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        ValueOperations valueOperations = redisTemplate.opsForValue();
        List<DishDto> dishDtoList = null;
        dishDtoList = (List<DishDto>) valueOperations.get(key);
        if (dishDtoList != null) {
            return R.success(dishDtoList);
        }
        // 构建查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        Long categoryId = dish.getCategoryId();
        queryWrapper.eq(categoryId != null, Dish::getCategoryId, categoryId);
        // 起售状态
        queryWrapper.eq(Dish::getStatus, 1);
        List<Dish> list = dishService.list(queryWrapper);
        // 将dish转为dishDto
        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            // 查询分类信息
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            // 查询
            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> flavorQueryWrapper = new LambdaQueryWrapper<DishFlavor>();
            flavorQueryWrapper.eq(DishFlavor::getDishId, id);
            List<DishFlavor> dishFlavors = dishFlavorService.list(flavorQueryWrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());
        // 查询出来放入redis中
        valueOperations.set(key, dishDtoList, 60L, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }
}
