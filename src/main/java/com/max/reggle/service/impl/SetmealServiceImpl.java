package com.max.reggle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.max.reggle.dto.SetmealDto;
import com.max.reggle.entity.Employee;
import com.max.reggle.entity.Setmeal;
import com.max.reggle.entity.SetmealDish;
import com.max.reggle.mapper.EmployeeMapper;
import com.max.reggle.mapper.SetmealMapper;
import com.max.reggle.service.EmployeeService;
import com.max.reggle.service.SetmealDishService;
import com.max.reggle.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public void saveSetmealAndSetmealDish(SetmealDto setmealDto) {
        //保存套餐信息
        this.save(setmealDto);

        //获取套餐详细
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
        List<SetmealDish> list = setmealDishList.stream().map((item) -> {
            LambdaQueryWrapper<Setmeal> setmealWrapper = new LambdaQueryWrapper<>();
            setmealWrapper.eq(Setmeal::getName, setmealDto.getName());
            Setmeal one = this.getOne(setmealWrapper);
            item.setSetmealId(one.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(list);


    }
}
