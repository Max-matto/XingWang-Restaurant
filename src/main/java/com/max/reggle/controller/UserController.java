package com.max.reggle.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.max.reggle.entity.User;
import com.max.reggle.service.UserService;
import com.max.reggle.utli.EmployeeHold;
import com.max.reggle.utli.R;
import com.max.reggle.utli.UserHold;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private JavaMailSender javaMailSender;

    private static final int NUMID = 9;

    @Autowired
    private UserService userService;



    /**
     * 发送给邮箱验证码
     * @param mail
     * @return
     */
    @GetMapping
    public R<String> getCode (@RequestParam("mail")String mail){
        log.info(mail);
        SimpleMailMessage message = new SimpleMailMessage();


        //生成验证码
        Object s = RandomUtil.randomNumbers(6);
        //保存到线程中
        long l = RandomUtil.randomLong(0, 91231);
        int length = String.valueOf(l).length();
        long id = Long.parseLong(    String.valueOf(l) + mail.substring(0, NUMID - length)) ;
        User user = new User();
        user.setId(id);
        user.setCode(s.toString());
        user.setMail(mail);
        userService.save(user);
       message.setText(s.toString());
        message.setTo(mail);
        message.setSubject("登录验证码信息");
        message.setFrom("1340898576@qq.com");
//        javaMailSender.send(message);

        return R.success("成功发送");
    }

    /**
     * 登录验证
     * @param request
     * @param user
     * @return
     */
    @PostMapping
    public R<String> loginUser(HttpServletRequest request,@RequestBody User user){
        log.info(user.toString());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getMail,user.getMail());
        User user1 =userService.getOne(wrapper);
        if (user1.getCode().equals(user.getCode()) && user1.getMail().equals(user.getMail())){

            request.getSession().setAttribute("user",user1.getId());
            return R.success("登录成功");
        }
        return R.error("登录失败");
    }



}
