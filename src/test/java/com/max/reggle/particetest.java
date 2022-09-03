package com.max.reggle;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.max.reggle.entity.Employee;
import com.max.reggle.entity.OrderDetail;
import com.max.reggle.entity.Orders;
import com.max.reggle.entity.User;
import com.max.reggle.utli.EmployeeHold;
import com.max.reggle.utli.UserHold;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.server.Session;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * @author 麦家宝
 * @version 1.0
 */
@SpringBootTest
public class particetest {

        @Test
        void AAA(){


            long orderId = IdWorker.getId();
            System.out.println(IdWorker.getTimeId());
            System.out.println(IdWorker.get32UUID());
            System.out.println(IdWorker.getIdStr());
            System.out.println(orderId);
        }
}
