package com.itzhy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itzhy.reggie.common.BaseContent;
import com.itzhy.reggie.common.R;
import com.itzhy.reggie.dto.OrderDto;
import com.itzhy.reggie.entity.Orders;
import com.itzhy.reggie.service.OrdersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    /**
     * @Description: 查询订单分页
     * @Author: zhy
     * @Date: 2022/11/2 10:48
     * @Param:  [page, pageSize]
     * @return: com.itzhy.reggie.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     */
    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize) {
        Page<OrderDto> orderDtoPage = ordersService.getPage(page,pageSize);
        return R.success(orderDtoPage);
    }
}
