package com.max.reggle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.max.reggle.common.CustomException;
import com.max.reggle.entity.Category;
import com.max.reggle.entity.Dish;
import com.max.reggle.entity.Employee;
import com.max.reggle.entity.Setmeal;
import com.max.reggle.mapper.CategoryMapper;
import com.max.reggle.mapper.EmployeeMapper;
import com.max.reggle.service.CategoryService;
import com.max.reggle.service.DishService;
import com.max.reggle.service.EmployeeService;
import com.max.reggle.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;


    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        //添加查询条件，更加分类id进行查询
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId,id);
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        int count = dishService.count(wrapper);
        if (count > 0){
            throw  new CustomException("已经关联了菜品");
            //抛出异常
        }

        LambdaQueryWrapper<Setmeal> setWrapper = new LambdaQueryWrapper<>();
        setWrapper.eq(Setmeal::getCategoryId,id);
        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        int count1 = setmealService.count(setWrapper);
        if (count1 > 0){
           throw new CustomException("已经关联了套餐");

            //抛出异常
        }
        //正常删除分类
        super.removeById(id);
    }
}
