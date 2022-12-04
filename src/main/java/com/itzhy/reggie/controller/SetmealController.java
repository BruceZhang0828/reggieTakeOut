package com.itzhy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itzhy.reggie.common.R;
import com.itzhy.reggie.dto.SetmealDto;
import com.itzhy.reggie.entity.Category;
import com.itzhy.reggie.entity.Dish;
import com.itzhy.reggie.entity.Setmeal;
import com.itzhy.reggie.service.CategoryService;
import com.itzhy.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhy
 * @description
 * @date 2022/10/28 15:58
 **/
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @CacheEvict(value = "setmealCache" ,allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("保存成功");
    }

    /**
     * @Description: 分页查询
     * @Author: zhy
     * @Date: 2022/10/28 22:17
     * @Param: [page, pageSize, name]
     * @return: com.itzhy.reggie.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 构建分页查询
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        // 条件查询
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        // 查询
        setmealService.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> setmealDtos = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = setmealDto.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(setmealDtos);
        return R.success(dtoPage);
    }

    /**
     * @Description: 根据id获取数据
     * @Author: zhy
     * @Date: 2022/10/29 11:13
     * @Param: [id]
     * @return: com.itzhy.reggie.common.R<com.itzhy.reggie.dto.SetmealDto>
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable long id) {
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return R.success(setmealDto);
    }

    /**
     * @Description: 更新数据
     * @Author: zhy
     * @Date: 2022/10/29 11:13
     * @Param: []
     * @return: com.itzhy.reggie.common.R<java.lang.String>
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        setmealService.updateWithDish(setmealDto);
        return R.success("更新操作完成");
    }

    /**
     * @Description: 删除套餐及其关联数据
     * @Author: zhy
     * @Date: 2022/10/28 23:17
     * @Param: [ids]
     * @return: com.itzhy.reggie.common.R<java.lang.String>
     */
    @DeleteMapping
    // 清空：allEntries = true
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.deleteWithDish(ids);
        return R.success("删除成功");
    }


    /**
     * @Description: 起售和停售
     * @Author: zhy
     * @Date: 2022/10/28 10:05
     * @Param: [status, ids]
     * @return: com.itzhy.reggie.common.R<java.lang.String>
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable int status, @RequestParam List<Long> ids) {
        List<Setmeal> setmeals = ids.stream().map((id) -> {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(id);
            setmeal.setStatus(status);
            return setmeal;
        }).collect(Collectors.toList());

        // 更新数据
        setmealService.updateBatchById(setmeals);
        return R.success("菜品数据更新成功");
    }

    @Cacheable(value = "setmealCache", key = "#categoryId+'_'+#status")
    @GetMapping("/list")
    public R<List<Setmeal>> list(long categoryId, int status) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, categoryId);
        queryWrapper.eq(Setmeal::getStatus, status);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }
}
