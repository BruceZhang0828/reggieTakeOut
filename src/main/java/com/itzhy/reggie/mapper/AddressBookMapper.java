package com.itzhy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itzhy.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhy
 * @description 地址管理类mapper
 * @date 2022/10/30 20:17
 **/
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
