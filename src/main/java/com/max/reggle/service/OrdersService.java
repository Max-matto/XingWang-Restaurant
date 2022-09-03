package com.max.reggle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.max.reggle.entity.AddressBook;
import com.max.reggle.entity.Orders;

/**
 * @author 麦家宝
 * @version 1.0
 */
public interface OrdersService extends IService<Orders> {

    void submit(Orders orders);

}
