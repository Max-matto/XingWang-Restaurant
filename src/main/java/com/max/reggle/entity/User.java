package com.max.reggle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Data
@TableName("user_mail")
public class User implements Serializable {

    private long id;
    //用户
    private String Mail;
    //验证码
    private String code;
}
