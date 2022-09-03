package com.max.reggle.dto;

import com.max.reggle.entity.Dish;
import com.max.reggle.entity.DishFlavor;
import com.max.reggle.entity.OrderDetail;
import com.max.reggle.entity.Orders;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto extends Orders {

   private List<OrderDetail> orderDetails;

   private Integer sumNum;


}
