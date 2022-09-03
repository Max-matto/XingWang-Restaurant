package com.max.reggle.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.max.reggle.dto.DishDto;
import com.max.reggle.dto.SetmealDto;
import com.max.reggle.entity.Category;
import com.max.reggle.entity.Dish;
import com.max.reggle.entity.DishFlavor;
import com.max.reggle.service.CategoryService;
import com.max.reggle.service.DishFlavorService;
import com.max.reggle.service.DishService;
import com.max.reggle.service.SetmealService;
import com.max.reggle.utli.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("dish")
public class DishFlavprController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;




    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveDishAndDishFlavor(dishDto);

        return R.success("新增菜品成功");

    }

    @GetMapping("/page")
    public R<Page> getList(String name,int page,int pageSize){
        //构造分页构造器对象
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();

        //添加过滤条件和排序条件
        wrapper.like(name != null, Dish::getName,name).orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        Page<Dish> page1 = dishService.page(dishPage, wrapper);

        //对象拷贝
        BeanUtil.copyProperties(dishPage,dishDtoPage,"records");
        List<Dish> records = page1.getRecords();
        //通过lamber循环数组并输出一个新的数组
        List<DishDto> dtoList = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtil.copyProperties(item, dishDto);
            Category byId = categoryService.getById(item.getCategoryId());
           //防止没有分类数据时 不报错
            if (byId != null){
               dishDto.setCategoryName(byId.getName());
           }
            return dishDto;

        }).collect(Collectors.toList());

        dishDtoPage.setRecords(dtoList);

        return  R.success(dishDtoPage);
    }

    /**
     * 菜品修改回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getDishDtoById(@PathVariable Long id){
        log.info(String.valueOf(id));
        //通过id获取菜品数据
        Dish byId = dishService.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtil.copyProperties(byId,dishDto);
        //通过dish_id获取菜品喜爱
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> list = dishFlavorService.list(wrapper);
        dishDto.setFlavors(list);
        //获取当前的菜品分类属性
        Category byId1 = categoryService.getById(dishDto.getCategoryId());
        dishDto.setCategoryName(byId1.getName());

        return R.success(dishDto);
    }


    @PutMapping
    public R<String> updateDishFlavor(@RequestBody DishDto dishDto){
        dishService.updateDishAndDishFlavor(dishDto);
        return R.success("更新成功！");
    }

    /**
     * 根据条件查询对应的菜品数据 和服务端菜品数据
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> getSetmealDto(@RequestParam("categoryId") long categoryId){
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId,categoryId);
        //查询启售状态 为 1
        wrapper.eq(Dish::getStatus,1).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = dishService.list(wrapper);

        List<DishDto> dishDtoList = dishList.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> list = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            BeanUtil.copyProperties(item, dishDto);
            dishDto.setFlavors(list);
            return dishDto;
        }).collect(Collectors.toList());


        return R.success(dishDtoList);
    }

}
