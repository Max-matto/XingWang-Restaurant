package com.max.reggle.utli;

import com.max.reggle.entity.Employee;
import org.springframework.stereotype.Component;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Component
public  class UserHold {
  private static  final ThreadLocal<Employee> t1  = new ThreadLocal<>();


  public static void saveUser(Employee employee){
      t1.set(employee);

  }
  public static Employee getUser(){

     return t1.get();

  }
  public static  void removeUser(){
      t1.remove();
  }


}
