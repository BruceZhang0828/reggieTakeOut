package com.itzhy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itzhy.reggie.entity.Orders;

/**
 * @author zhy
 * @description
 * @date 2022/11/01 23:15
 **/
public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
