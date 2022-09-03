package com.max.reggle.controller;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.max.reggle.entity.Employee;
import com.max.reggle.service.EmployeeService;
import com.max.reggle.utli.R;
import com.max.reggle.utli.UserHold;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sun.security.provider.MD5;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    /**
     * 登录功能
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        //根据页面提交的用户username查询数据库
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.eq("username",employee.getUsername());
        Employee emp = employeeService.getOne(wrapper);
        //如果没有查询到则放回登录失败结果
        if (emp == null){
            return R.error("登录失败");
        }
        //密码比对
        if (!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }
        //查看员工状态，如果为禁用则放回错误信息
        if (emp.getStatus() != 1){
            return R.error("该员工已被禁用");
        }
        request.getSession().setAttribute("employee",emp.getId());
        //将信息存入线程中
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清除session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        UserHold.removeUser();
        return R.success("账号退出");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest httpServletRequest,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());

        //设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser((long)httpServletRequest.getSession().getAttribute("employee"));
//        employee.setUpdateUser((long)httpServletRequest.getSession().getAttribute("employee"));
        employeeService.save(employee);
        return R.success("添加数据成功！");

    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
   @GetMapping("/page")
    public R<Page> Page(int page,int pageSize,String name){
        log.info("页数{}数量{}",page,pageSize);
        //分页构造器
       Page page1 = new Page(page,  pageSize);
       //条件构造器
       LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper();
       //过滤条件
       wrapper.like(Strings.isNotEmpty(name), Employee::getName, name);
       //添加排序条件
       wrapper.orderByDesc(Employee::getUpdateTime);
       //执行查询
       Page page2 = employeeService.page(page1, wrapper);
       return   R.success(page2) ;
    }

    @PutMapping
    public R<String> ChangeStatus(HttpServletRequest request,@RequestBody Employee employee){

       log.info(employee.toString());

        Long userId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(userId);
        boolean b = employeeService.updateById(employee);

        if (b == false){
            return R.error("操作失败");
        }
        return R.success("操作成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable long id){
        Employee employee = employeeService.getById(id);
        if (employee != null){
            return R.success(employee);
        }
        return R.error("操作错误");
    }
}
