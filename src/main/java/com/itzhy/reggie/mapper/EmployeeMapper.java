package com.itzhy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itzhy.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhy
 * @description
 * @date 2022/10/15 20:11
 **/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
