package com.itzhy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itzhy.reggie.common.R;
import com.itzhy.reggie.entity.Employee;
import com.itzhy.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
     * @Param: [request]
     * @return: com.itzhy.reggie.common.R<java.lang.String>
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出登录。");
    }

    /**
     * @Description: 新增员工服务
     * @Author: zhy
     * @Date: 2022/10/16 21:31
     * @Param: [request, employee]
     * @return: com.itzhy.reggie.common.R<java.lang.String>
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工信息，{}", employee.toString());
        // 设置初始密码123456，需要进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //
        LocalDateTime now = LocalDateTime.now();
        employee.setCreateTime(now);
        employee.setUpdateTime(now);
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功！");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 构造分页器
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), Employee::getName, name);
        // 添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);

    }

    /**
     * @Description: 根据ID修改员工信息
     * @Author: zhy
     * @Date: 2022/10/19 14:41
     * @Param: [employee]
     * @return: com.itzhy.reggie.common.R<java.lang.String>
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("修改成功。");
    }

    /**
     * @Description: 根据员工id查询员工信息
     * @Author: zhy
     * @Date: 2022/10/19 15:51
     * @Param: [id]
     * @return: com.itzhy.reggie.common.R<com.itzhy.reggie.entity.Employee>
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工信息。。。");
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询到对应的员工信息。");
    }
}
