//package com.max.reggle.comfig;
//
//import com.max.reggle.entity.Employee;
//import com.max.reggle.utli.UserHold;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * @author 麦家宝
// * @version 1.0
// */
//public class refeshInterpert implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        Integer employeeById = (Integer) request.getSession().getAttribute("employee");
//        if (employeeById == null){
//            return true;
//        }
//
//        //如果存在的话存入线程
//        UserHold.saveUser(employee);
//        return true;
//    }
//
//}
