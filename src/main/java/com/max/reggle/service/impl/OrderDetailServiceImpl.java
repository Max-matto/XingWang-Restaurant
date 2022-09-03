package com.max.reggle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.max.reggle.entity.OrderDetail;
import com.max.reggle.entity.Orders;
import com.max.reggle.mapper.OrderDetailMapper;
import com.max.reggle.mapper.OrdersMapper;
import com.max.reggle.service.OrderDetailService;
import com.max.reggle.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
