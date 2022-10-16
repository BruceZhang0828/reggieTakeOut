package com.itzhy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itzhy.reggie.entity.Employee;
import com.itzhy.reggie.mapper.EmployeeMapper;
import com.itzhy.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author zhy
 * @description
 * @date 2022/10/15 20:15
 **/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService {
}
