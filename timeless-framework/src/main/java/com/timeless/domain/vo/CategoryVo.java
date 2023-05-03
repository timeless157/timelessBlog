package com.timeless.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author timeless
 * @create 2022-12-05 22:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVo {

    private Long id;
    //分类名
    private String name;

    private String description;

}
