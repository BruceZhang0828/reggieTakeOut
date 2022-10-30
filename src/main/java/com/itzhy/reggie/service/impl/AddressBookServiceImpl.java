package com.itzhy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itzhy.reggie.entity.AddressBook;
import com.itzhy.reggie.mapper.AddressBookMapper;
import com.itzhy.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author zhy
 * @description 地址管理serviceImpl
 * @date 2022/10/30 20:15
 **/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper,AddressBook> implements AddressBookService {
}
