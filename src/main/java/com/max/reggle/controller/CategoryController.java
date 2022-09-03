package com.max.reggle.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.max.reggle.entity.Category;
import com.max.reggle.entity.Employee;
import com.max.reggle.service.CategoryService;
import com.max.reggle.service.EmployeeService;
import com.max.reggle.utli.R;
import com.max.reggle.utli.UserHold;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取分类的分页数据
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> getPageList(int page , int pageSize ){
        log.info("{}，{}",page,pageSize);
        Page<Category> categoryPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Category::getSort);
        Page<Category> page1 = categoryService.page(categoryPage,wrapper);
        return R.success(page1);
    }
    @PostMapping
    public R<String> addCategory(@RequestBody Category category){
        log.info(category.toString());
        boolean save = categoryService.save(category);
        return R.success("添加成功");
    }
    @DeleteMapping
    public R<String> deleteCategoty(long ids){
        categoryService.remove(ids);

        return R.success("删除成功");

    }


    /**
     * 更新分类
     * @param category
     * @return
     */
    @PutMapping
    public R<String> updateCategoty(@RequestBody Category category){
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getId,category.getId());
        boolean update = categoryService.update(category, wrapper);
        if (!update){
            return R.error("更新失败");
        }
        return R.success("更新成功");


    }

    @GetMapping("/list")
    public R<List<Category>> getListOfCategoryType(Category category){
        //建立查询
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category.getType() != null,Category::getType,category.getType()).orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> cates = categoryService.list(wrapper);

        return R.success(cates);
    }

}
