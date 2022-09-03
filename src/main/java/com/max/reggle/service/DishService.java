package com.max.reggle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.max.reggle.dto.DishDto;
import com.max.reggle.entity.Category;
import com.max.reggle.entity.Dish;
import com.max.reggle.mapper.DishMapper;

/**
 * @author 麦家宝
 * @version 1.0
 */
public interface DishService extends IService<Dish> {
    void saveDishAndDishFlavor(DishDto dishDto);
    void updateDishAndDishFlavor(DishDto dishDto);
}

