package com.itzhy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itzhy.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.Mapping;

/**
 * @author zhy
 * @description mapper
 * @date 2022/11/01 23:11
 **/
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
