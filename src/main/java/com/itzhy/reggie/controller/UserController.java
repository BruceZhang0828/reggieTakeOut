package com.itzhy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itzhy.reggie.common.R;
import com.itzhy.reggie.entity.User;
import com.itzhy.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author zhy
 * @description
 * @date 2022/10/30 11:18
 **/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        Object phoneObj = map.get("phone");
        Object code = map.get("code");
        if (phoneObj != null) {
            String phone = phoneObj.toString();
            Object attributeCode = session.getAttribute(phone);
            if (attributeCode != null && attributeCode.equals(code)) {
                log.info("验证码比对完成");
            }
            // 没有传code
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User one = userService.getOne(queryWrapper);
            if (one == null) {
                one = new User();
                one.setPhone(phone);
                one.setStatus(1);
                userService.save(one);
            }
            session.setAttribute("user", one.getId());
            return R.success(one);
        }
        return R.error("登录失败");
    }
}
