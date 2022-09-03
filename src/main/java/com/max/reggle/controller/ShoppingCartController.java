package com.max.reggle.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.max.reggle.entity.Category;
import com.max.reggle.entity.ShoppingCart;
import com.max.reggle.entity.User;
import com.max.reggle.service.ShoppingCartService;
import com.max.reggle.utli.EmployeeHold;
import com.max.reggle.utli.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 麦家宝
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 添加进购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> addShoppingCart(@RequestBody ShoppingCart shoppingCart){
        //获取加入的用户的id
        long userid = (long)EmployeeHold.getUser().getId();
        shoppingCart.setUserId(userid);
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userid);
        //判断加入的是菜品还是套餐
        if (shoppingCart.getDishId() != null){
            //菜品--判断菜品、用户、口味是否有相同数据。
            wrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            //套餐--判断是否有相同数据
            wrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(wrapper);
        LambdaUpdateWrapper<ShoppingCart> shoppingCartUpdateWrapper = new LambdaUpdateWrapper<>();

        //如果一样增加数量
        if (one != null){
            Integer number = one.getNumber();
            one.setNumber(number + 1);
            shoppingCartService.updateById(one);
        }else {
            //如果不一样，重新插入数据
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }
        return R.success(one);
    }

    /**
     * 显示购物车列表
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> getCategoryList(){
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,EmployeeHold.getUser().getId());

        List<ShoppingCart> list = shoppingCartService.list(wrapper);

        return R.success(list);


    }

    /**
     * 减少购物车的物品
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> subDishOrSetmeal(@RequestBody ShoppingCart shoppingCart){

        LambdaQueryWrapper<ShoppingCart> shoppingCartWrapper = new LambdaQueryWrapper<>();
        shoppingCartWrapper.eq(ShoppingCart::getUserId,EmployeeHold.getUser().getId());
        if (shoppingCart.getDishId() != null){
            shoppingCartWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            shoppingCartWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart one = shoppingCartService.getOne(shoppingCartWrapper);
        Integer number = one.getNumber();

        if (number == 1){
           shoppingCartService.remove(shoppingCartWrapper);
            ShoppingCart shoppingCart1 = new ShoppingCart();
            shoppingCart1.setNumber(0);
            return R.success(shoppingCart1);
        }

        one.setNumber(number - 1);
        shoppingCartService.updateById(one);
        return R.success(shoppingCartService.getOne(shoppingCartWrapper));




    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("clean")
    public R<String> cleanShoppingCart(){
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,EmployeeHold.getUser().getId());
        shoppingCartService.remove(wrapper);

        return R.success("已清空！");



    }

}
