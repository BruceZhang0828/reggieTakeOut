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

    @GetMapping("/list")
    public R<List> list() {
        List<Object> list = new ArrayList<>();
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
}
