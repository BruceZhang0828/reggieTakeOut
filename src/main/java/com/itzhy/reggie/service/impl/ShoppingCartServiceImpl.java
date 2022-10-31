package com.itzhy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itzhy.reggie.entity.ShoppingCart;
import com.itzhy.reggie.mapper.ShoppingCartMapper;
import com.itzhy.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author zhy
 * @description
 * @date 2022/10/31 22:22
 **/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
