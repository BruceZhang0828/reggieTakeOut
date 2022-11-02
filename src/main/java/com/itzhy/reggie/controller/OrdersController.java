package com.itzhy.reggie.controller;

import com.itzhy.reggie.common.R;
import com.itzhy.reggie.entity.Orders;
import com.itzhy.reggie.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhy
 * @description
 * @date 2022/11/01 23:20
 **/
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;
    /**
     * @Description: 下单操作
     * @Author: zhy
     * @Date: 2022/11/2 9:53
     * @Param:  [orders]
     * @return: com.itzhy.reggie.common.R<java.lang.String>
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        ordersService.submit(orders);
        return R.success("下单成功。");
    }
}
