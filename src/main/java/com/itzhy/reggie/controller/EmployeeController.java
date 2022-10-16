package com.itzhy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itzhy.reggie.common.R;
import com.itzhy.reggie.entity.Employee;
import com.itzhy.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhy
 * @description
 * @date 2022/10/15 20:17
 **/
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // 1.对password进行Hd5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 2.查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee one = employeeService.getOne(queryWrapper);
        // 3. 如果没有查询到直接返回
        if (one == null) {
            return R.error("登录失败");
        }
        // 4.密码比对
        if (!one.getPassword().equals(password)) {
            return R.error("登录失败");
        }
        // 5.查看员工状态，如果禁用状态，则返回员工禁用结果
        if (one.getStatus() == 0) {
            return R.error("账号已禁用！");
        }
        // 6.如果登录成功将用户id放入session中
        request.getSession().setAttribute("employee", one.getId());
        return R.success(one);
    }
    /**
     * @Description: 登录后退出功能
     * @Author: zhy
     * @Date: 2022/10/16 10:26
     * @Param:  [request]
     * @return: com.itzhy.reggie.common.R<java.lang.String>
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出登录。");
    }
}
