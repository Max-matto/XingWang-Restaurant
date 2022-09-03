package com.max.reggle.dto;

import com.max.reggle.entity.Setmeal;
import com.max.reggle.entity.SetmealDish;
import lombok.Data;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes = new ArrayList<>();
    private String categoryName;

}
