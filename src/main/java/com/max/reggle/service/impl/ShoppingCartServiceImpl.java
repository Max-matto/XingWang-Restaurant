package com.max.reggle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.max.reggle.entity.AddressBook;
import com.max.reggle.entity.ShoppingCart;
import com.max.reggle.mapper.AddressBookMapper;
import com.max.reggle.mapper.ShoppingCartMapper;
import com.max.reggle.service.AddressBookService;
import com.max.reggle.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
