package com.max.reggle.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.max.reggle.entity.AddressBook;
import com.max.reggle.entity.Category;
import com.max.reggle.entity.User;
import com.max.reggle.service.AddressBookService;
import com.max.reggle.service.CategoryService;
import com.max.reggle.utli.EmployeeHold;
import com.max.reggle.utli.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     */
    @PostMapping
    public R<AddressBook> addressBook(HttpServletRequest request, @RequestBody AddressBook addressBook){
        addressBook.setUserId((long)request.getSession().getAttribute("user"));
        addressBookService.save(addressBook);


        return R.success(addressBook);
    }

    /**
     * 列出地址
     * @param request
     * @param addressBook
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(HttpServletRequest request,AddressBook addressBook){
        addressBook.setUserId((long)request.getSession().getAttribute("user"));
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId,addressBook.getUserId());
        List<AddressBook> list = addressBookService.list(wrapper);
        return R.success(list);

    }

    /**
     * 默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public R<AddressBook> default12(@RequestBody AddressBook addressBook){
        //现是所有的地址都设置为否
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId,EmployeeHold.getUser().getId());
        wrapper.set(AddressBook::getIsDefault,0);

        addressBookService.update(wrapper);
        //然后将选中的设置为1
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getId,addressBook.getId()).set(AddressBook::getIsDefault,1);
        addressBookService.update(updateWrapper);

        return R.success(addressBook);

    }
    /**
     * 订单显示默认地址
     * @param
     * @return
     */
    @GetMapping("/default")
    public R<AddressBook> defaultOrder(){
        //现是所有的地址都设置为否
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getIsDefault,1).eq(AddressBook::getUserId,EmployeeHold.getUser().getId());
        AddressBook one = addressBookService.getOne(wrapper);

        return R.success(one);

    }

}
