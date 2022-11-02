package com.itzhy.reggie.dto;

import com.itzhy.reggie.entity.OrderDetail;
import com.itzhy.reggie.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * @author zhy
 * @description
 * @date 2022/11/02 10:27
 **/
@Data
public class OrderDto extends Orders {
    public List<OrderDetail> orderDetails;
}
