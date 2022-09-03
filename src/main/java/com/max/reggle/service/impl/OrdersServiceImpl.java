package com.max.reggle.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.max.reggle.common.CustomException;
import com.max.reggle.entity.AddressBook;
import com.max.reggle.entity.OrderDetail;
import com.max.reggle.entity.Orders;
import com.max.reggle.entity.ShoppingCart;
import com.max.reggle.mapper.AddressBookMapper;
import com.max.reggle.mapper.OrdersMapper;
import com.max.reggle.service.*;
import com.max.reggle.utli.EmployeeHold;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Override
    public void submit(Orders orders) {
        //获得当前用户id
        long userId = EmployeeHold.getUser().getId();

        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> list = shoppingCartService.list(wrapper);

        if (list == null || list.size() == 0){
            throw new CustomException("购物车无数据，请点你想要的菜品");
        }
        long orderId = IdWorker.getId();//订单号
        AtomicInteger amount = new AtomicInteger();
        List<OrderDetail> orderDetails = list.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setImage(item.getImage());
            orderDetail.setName(item.getName());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        //获取地址的数据
        AddressBook address = addressBookService.getById(orders.getAddressBookId());
        //向订单插入一条数据
        orders.setId(orderId);
        orders.setNumber(String.valueOf(orderId));
        orders.setAddress(address.getDetail());
        orders.setPhone(address.getPhone());
        orders.setUserId(userId);
        orders.setConsignee(address.getConsignee());
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));

        this.save(orders);

        //向订单详情插入多条数据
        orderDetailService.saveBatch(orderDetails);

        LambdaQueryWrapper<ShoppingCart> shoppingCartWrapper = new LambdaQueryWrapper<>();
        shoppingCartWrapper.eq(ShoppingCart::getUserId,EmployeeHold.getUser().getId());
        shoppingCartService.remove(shoppingCartWrapper);
    }
}
