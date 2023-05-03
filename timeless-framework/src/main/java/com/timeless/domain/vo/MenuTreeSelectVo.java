package com.timeless.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author timeless
 * @create 2022-12-17 15:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuTreeSelectVo {

    private Long id;

//    private String label;
    private String menuName;

    private Long parentId;

    private List<MenuTreeSelectVo> children;


}
