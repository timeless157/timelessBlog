package com.timeless.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author timeless
 * @create 2022-12-17 20:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuTreeSelectVo {

    private List<MenuTreeSelectVo> menus;

    private List<Long> checkedKeys;

}
