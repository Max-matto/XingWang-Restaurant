package com.max.reggle.service;

import cn.hutool.extra.mail.Mail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.max.reggle.dto.DishDto;
import com.max.reggle.entity.Dish;
import com.max.reggle.entity.User;

/**
 * @author 麦家宝
 * @version 1.0
 */
public interface UserService extends IService<User> {
    void sendCode(String mail);
}

