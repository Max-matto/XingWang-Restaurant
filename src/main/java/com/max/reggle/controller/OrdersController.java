package com.max.reggle.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.max.reggle.dto.OrderDto;
import com.max.reggle.entity.AddressBook;
import com.max.reggle.entity.OrderDetail;
import com.max.reggle.entity.Orders;
import com.max.reggle.service.AddressBookService;
import com.max.reggle.service.OrderDetailService;
import com.max.reggle.service.OrdersService;
import com.max.reggle.utli.EmployeeHold;
import com.max.reggle.utli.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService ordersDetailService;

  @PostMapping("/submit")
    public R<String> submitOrder(@RequestBody Orders orders){
      ordersService.submit(orders);

      return R.success("下单成功");

  }

  @GetMapping("/userPage")
    public R<Page<OrderDto>> getOrders(int page,int pageSize){
      Page<Orders> ordersPage = new Page<>(page,pageSize);
    Page<OrderDto> orderDtoPage = new Page<>();
    LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(Orders::getUserId,EmployeeHold.getUser().getId());
      Page<Orders> page1 = ordersService.page(ordersPage, wrapper);
    BeanUtil.copyProperties(page1,orderDtoPage,"records");
    List<Orders> records = page1.getRecords();
    //遍历每个订单
    List<OrderDto> collect = records.stream().map((item -> {
      //创建OrderDto
      OrderDto orderDto = new OrderDto();
      //复制订单基本信息
      BeanUtil.copyProperties(item, orderDto);
      LambdaQueryWrapper<OrderDetail> orderDetailWrapper = new LambdaQueryWrapper<>();
      orderDetailWrapper.eq(OrderDetail::getOrderId, item.getId());
      //复制订单菜品信息
      List<OrderDetail> list = ordersDetailService.list(orderDetailWrapper);
      orderDto.setOrderDetails(list);
      //获取每个菜品数量的总和
      int sumNumber = 0;
      for (OrderDetail orderDetail : list) {
        Integer number = orderDetail.getNumber();
        sumNumber += number;
      }
      orderDto.setSumNum(sumNumber);

      return orderDto;

    })).collect(Collectors.toList());
    //将信息载入分页orderDtoPage

    orderDtoPage.setRecords(collect);

    return R.success(orderDtoPage);


  }

  @GetMapping("/page")
  public R<Page<Orders>> getOrdersList(int page,int pageSize){

    Page<Orders> ordersPage = new Page<>(page, pageSize);
    Page<Orders> page1 = ordersService.page(ordersPage);
    return R.success(page1);


  }




}
