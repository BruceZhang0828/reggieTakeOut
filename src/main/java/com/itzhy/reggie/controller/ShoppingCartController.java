package com.itzhy.reggie.controller;

import com.itzhy.reggie.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/list")
    public R<List> list() {
        List<Object> list = new ArrayList<>();
        return R.success(list);
    }
}
