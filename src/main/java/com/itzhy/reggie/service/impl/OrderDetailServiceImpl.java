package com.itzhy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itzhy.reggie.entity.OrderDetail;
import com.itzhy.reggie.mapper.OrderDetailMapper;
import com.itzhy.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author zhy
 * @description
 * @date 2022/11/01 23:18
 **/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
