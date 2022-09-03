package com.max.reggle.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.max.reggle.common.CustomException;
import com.max.reggle.dto.SetmealDto;
import com.max.reggle.entity.Category;
import com.max.reggle.entity.Setmeal;
import com.max.reggle.entity.SetmealDish;
import com.max.reggle.service.CategoryService;
import com.max.reggle.service.SetmealDishService;
import com.max.reggle.service.SetmealService;
import com.max.reggle.utli.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 麦家宝
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page<SetmealDto>> getSetmealPage(int page,int pageSize,String name){
        //创建page对象
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Setmeal> setmealWrapper = new LambdaQueryWrapper<>();
        if (name != null){
            setmealWrapper.eq(Setmeal::getName,name);
        }
        //读取数据到DTOPage中
        Page<Setmeal> page1 = setmealService.page(setmealPage, setmealWrapper);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtil.copyProperties(page1,setmealDtoPage,"records");

        //现获取page1中的Setmeal对象
        List<Setmeal> records = page1.getRecords();
        //通过Lamber的map函数将数据复制到setmealDto中，并添加CategoryName
        List<SetmealDto> collect = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtil.copyProperties(item, setmealDto);
            Category byId = categoryService.getById(item.getCategoryId());
            setmealDto.setCategoryName(byId.getName());
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(collect);

        return  R.success(setmealDtoPage);
    }

    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.saveSetmealAndSetmealDish(setmealDto);
        return R.success("添加成功");
    }

    /**
     * 数据回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getSetmelDtoByid(@PathVariable("id") long id){
       //套餐信息
        Setmeal byId = setmealService.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtil.copyProperties(byId,setmealDto);
        //套餐详细信息添加查询条件
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        List<SetmealDish> list = setmealDishService.list(wrapper);
        setmealDto.setSetmealDishes(list);
        //套餐分类
        LambdaQueryWrapper<Category> categoryWrapper = new LambdaQueryWrapper<>();
        categoryWrapper.eq(Category::getId,setmealDto.getCategoryId());
        Category category = categoryService.getOne(categoryWrapper);
        setmealDto.setCategoryName(category.getName());
        return R.success(setmealDto);
    }

    /**
     * 更新套餐数据
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> updateSetmeal(@RequestBody SetmealDto setmealDto){
        //更新套餐表
        LambdaQueryWrapper<Setmeal> setmealWrapper = new LambdaQueryWrapper<>();
        setmealWrapper.eq(Setmeal::getId,setmealDto.getId());
        setmealService.update(setmealDto,setmealWrapper);
        //更新套餐菜品表
        //删除原来的数据
        LambdaQueryWrapper<SetmealDish> setmealDishWrapper = new LambdaQueryWrapper<>();
        setmealDishWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(setmealDishWrapper);
        //保存数据
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<SetmealDish> collect = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        if (collect.size() > 0){
            setmealDishService.saveBatch(collect);
        }
        return R.success("更新成功");


    }

    /**
     * 根据ids删除套餐
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> deleteSetmeal(@RequestParam("ids")List<Long> id){
        log.info("ids:{}",id);

        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> setmealWrapper = new LambdaQueryWrapper<>();
        setmealWrapper.in(Setmeal::getId,id).eq(Setmeal::getStatus,1);

        //如果不能删除，抛出一个业务异常
        int count = setmealService.count(setmealWrapper);
        if (count >0){
            throw  new CustomException("已售状态，不能删除");
        }

        //如果可以删除，现删除套餐表中的数据
        setmealService.removeByIds(id);
        //删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SetmealDish::getSetmealId,id);
        setmealDishService.remove(wrapper);



        return R.success("删除成功！");
    }

    /**
     * 切换状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{num}")
    public R<String> updateStatus(@PathVariable("num")int status,@RequestParam("ids")long id){
        log.info("path{},ids{}",status,id);
        UpdateWrapper<Setmeal> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",id).set("status",status);
        setmealService.update(wrapper);

        return R.success("修改状态成功");
    }

    /**
     * 服务端页面数据
     * @param categoryId
     * @param status
     * @return
     */
    @GetMapping("list")
    public R<List<SetmealDto>> getListSetmeal(long categoryId,int status){
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId,categoryId).eq(Setmeal::getStatus,status);
        List<Setmeal> list = setmealService.list(wrapper);
        List<SetmealDto> collect = list.stream().map((item -> {
            LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, item.getId());
            SetmealDto setmealDto = new SetmealDto();
            BeanUtil.copyProperties(item, setmealDto);
            List<SetmealDish> list1 = setmealDishService.list(setmealDishLambdaQueryWrapper);
            setmealDto.setSetmealDishes(list1);
            return setmealDto;

        })).collect(Collectors.toList());
        return R.success(collect);


    }

    /**
     * 服务端套餐详情
     * @param Id
     * @return
     */
    @GetMapping("/dish/{id}")
    public R<SetmealDto> getSetinformmeal(@PathVariable("id") long Id){
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getId,Id);
        Setmeal one = setmealService.getOne(wrapper);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, one.getId());
        SetmealDto setmealDto = new SetmealDto();
        BeanUtil.copyProperties(one, setmealDto);
        List<SetmealDish> list1 = setmealDishService.list(setmealDishLambdaQueryWrapper);
        setmealDto.setSetmealDishes(list1);

        return R.success(setmealDto);


    }

}
