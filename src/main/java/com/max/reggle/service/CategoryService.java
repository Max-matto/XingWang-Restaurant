package com.max.reggle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.max.reggle.entity.Category;
import com.max.reggle.entity.Employee;

/**
 * @author 麦家宝
 * @version 1.0
 */
public interface CategoryService extends IService<Category> {

    void remove(Long id);

}
