package com.max.reggle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.max.reggle.dto.DishDto;
import com.max.reggle.entity.Dish;
import com.max.reggle.entity.DishFlavor;
import com.max.reggle.mapper.DishFlavorMapper;
import com.max.reggle.mapper.DishMapper;
import com.max.reggle.service.DishFlavorService;
import com.max.reggle.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {

}
