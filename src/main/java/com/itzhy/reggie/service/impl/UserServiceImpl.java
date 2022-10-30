package com.itzhy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itzhy.reggie.entity.User;
import com.itzhy.reggie.mapper.UserMapper;
import com.itzhy.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author zhy
 * @description
 * @date 2022/10/30 11:17
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
