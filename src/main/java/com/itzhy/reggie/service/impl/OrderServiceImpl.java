package com.itzhy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itzhy.reggie.common.BaseContent;
import com.itzhy.reggie.common.CustomException;
import com.itzhy.reggie.entity.*;
import com.itzhy.reggie.mapper.OrdersMapper;
import com.itzhy.reggie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author zhy
 * @description
 * @date 2022/11/01 23:16
 **/
@Service
public class OrderServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * @Description: 下单操作
     * @Author: zhy
     * @Date: 2022/11/2 9:51
     * @Param: [orders]
     * @return: void
     */
    @Transactional
    @Override
    public void submit(Orders orders) {
        // 获取当前用户id
        Long curId = BaseContent.getCurId();
        // 查询购物车
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, curId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(queryWrapper);
        if (shoppingCarts == null || shoppingCarts.size() <= 0) {
            throw new CustomException("购物车为空，不能下单");
        }
        User user = userService.getById(curId);
        //
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("用户地址有误，不能下单。");
        }
        // 生成一个订单id
        long orderId = IdWorker.getId();
        // 总计一下金额
        AtomicInteger amount = new AtomicInteger(0);
        // 构建订单详情数据
        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setImage(item.getImage());
            orderDetail.setName(item.getName());
            BigDecimal itemAmount = item.getAmount();
            orderDetail.setAmount(itemAmount);
            // 统计金额
            amount.addAndGet(itemAmount.multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        // 构建订单数据
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        // 设置总金额
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(curId);
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        // 设置地址
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        // 保存订单
        this.save(orders);
        // 保存订单详情
        orderDetailService.saveBatch(orderDetails);
        // 清空购物车
        shoppingCartService.remove(queryWrapper);
    }
}
