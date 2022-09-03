package com.max.reggle.utli;

import com.max.reggle.entity.Employee;
import com.max.reggle.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Component
public  class EmployeeHold {
  private static  ThreadLocal<User> t1  = new ThreadLocal<>();


  public static void saveUser(User employee){
      t1.set(employee);

  }
  public static User getUser(){

     return t1.get();

  }
  public static  void removeUser(){
      t1.remove();
  }


}
