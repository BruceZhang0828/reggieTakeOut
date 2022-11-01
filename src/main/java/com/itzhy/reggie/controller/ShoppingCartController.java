package com.itzhy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itzhy.reggie.common.BaseContent;
import com.itzhy.reggie.common.R;
import com.itzhy.reggie.entity.ShoppingCart;
import com.itzhy.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhy
 * @description
 * @date 2022/10/30 20:39
 **/
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * @Description: 查询购物车
     * @Author: zhy
     * @Date: 2022/11/1 21:24
     * @Param: []
     * @return: com.itzhy.reggie.common.R<java.util.List < com.itzhy.reggie.entity.ShoppingCart>>
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        // 查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContent.getCurId());
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        //获取当前用户id
        Long curId = BaseContent.getCurId();
        shoppingCart.setUserId(curId);
        // 查询是否已经保存
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        }
        Long setmealId = shoppingCart.getSetmealId();
        if (setmealId != null) {
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }
        queryWrapper.eq(ShoppingCart::getUserId, curId);
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        if (one != null) {
            // 保存的话数量+1
            Integer number = one.getNumber();
            one.setNumber(number + 1);
            shoppingCartService.updateById(one);
        } else {
            // 没有就保存
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }
        return R.success(one);
    }

    /**
     * @Description: 清空购物车成功
     * @Author: zhy
     * @Date: 2022/11/1 21:31
     * @Param: []
     * @return: com.itzhy.reggie.common.R<java.lang.String>
     */
    @DeleteMapping("/clean")
    public R<String> clean() {
        // 查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContent.getCurId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        //获取当前用户id
        Long curId = BaseContent.getCurId();
        shoppingCart.setUserId(curId);
        // 查询是否已经保存
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        }
        Long setmealId = shoppingCart.getSetmealId();
        if (setmealId != null) {
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }
        queryWrapper.eq(ShoppingCart::getUserId, curId);
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);

        // 保存的话数量 -1
        Integer number = one.getNumber();
        one.setNumber(number - 1);
        if (number == 1) {
            // 最后一个就删除数据
            shoppingCartService.removeById(one.getId());
        } else {
            one.setNumber(number - 1);
            shoppingCartService.updateById(one);
        }
        return R.success(one);
    }
}
