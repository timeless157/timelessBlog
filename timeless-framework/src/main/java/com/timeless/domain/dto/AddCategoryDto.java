package com.timeless.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author timeless
 * @create 2022-12-18 1:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryDto {
    //分类名
    private String name;
    //描述
    private String description;
    //状态0:正常,1禁用
    private String status;
}
