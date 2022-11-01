package com.itzhy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itzhy.reggie.entity.Orders;
import com.itzhy.reggie.mapper.OrdersMapper;
import com.itzhy.reggie.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * @author zhy
 * @description
 * @date 2022/11/01 23:16
 **/
@Service
public class OrderServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
}
