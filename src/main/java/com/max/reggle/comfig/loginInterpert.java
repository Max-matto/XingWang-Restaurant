package com.max.reggle.comfig;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.max.reggle.entity.Employee;
import com.max.reggle.entity.User;
import com.max.reggle.service.UserService;
import com.max.reggle.utli.EmployeeHold;
import com.max.reggle.utli.UserHold;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

/**
 * @author 麦家宝
 * @version 1.0
 */
public class loginInterpert implements HandlerInterceptor {
    @Autowired
    private   UserService userService;
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        User user = EmployeeHold.getUser();
        if (user != null){
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getId,user.getId());
            userService.remove(wrapper);
        }

    }
}
