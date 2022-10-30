package com.itzhy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itzhy.reggie.entity.Employee;
import com.itzhy.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhy
 * @description
 * @date 2022/10/15 20:11
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
