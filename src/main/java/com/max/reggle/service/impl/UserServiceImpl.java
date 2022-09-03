package com.max.reggle.service.impl;

import cn.hutool.extra.mail.Mail;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.max.reggle.common.CustomException;
import com.max.reggle.entity.Category;
import com.max.reggle.entity.Dish;
import com.max.reggle.entity.Setmeal;
import com.max.reggle.entity.User;
import com.max.reggle.mapper.CategoryMapper;
import com.max.reggle.mapper.UserMapper;
import com.max.reggle.service.CategoryService;
import com.max.reggle.service.DishService;
import com.max.reggle.service.SetmealService;
import com.max.reggle.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public void sendCode(String mail) {

    }
}
