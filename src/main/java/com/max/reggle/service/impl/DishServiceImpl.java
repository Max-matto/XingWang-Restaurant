package com.max.reggle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.max.reggle.dto.DishDto;
import com.max.reggle.entity.Category;
import com.max.reggle.entity.Dish;
import com.max.reggle.entity.DishFlavor;
import com.max.reggle.mapper.CategoryMapper;
import com.max.reggle.mapper.DishFlavorMapper;
import com.max.reggle.mapper.DishMapper;
import com.max.reggle.service.CategoryService;
import com.max.reggle.service.DishFlavorService;
import com.max.reggle.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;



    /**
     * 保存菜品，同时保存对应的口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveDishAndDishFlavor(DishDto dishDto) {
        //添加菜品表
        this.save(dishDto);
        //获取菜品喜好表，并添加上菜品id
        Long dish_id = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) -> {
            item.setDishId(dish_id);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);


    }

    /**
     * 保存菜品
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateDishAndDishFlavor(DishDto dishDto) {
        //更新菜品表
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getId,dishDto.getId());
        this.update(dishDto, wrapper);

        //删除喜爱表的数据
        LambdaQueryWrapper<DishFlavor> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(deleteWrapper);

        //添加当前提交过来的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        //因为接收到的数据flavor没有菜品id，需要map去循环插入
        List<DishFlavor> collect = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(collect);


    }


}
