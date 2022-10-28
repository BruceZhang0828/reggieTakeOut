package com.itzhy.reggie.dto;

import com.itzhy.reggie.entity.Setmeal;
import com.itzhy.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
