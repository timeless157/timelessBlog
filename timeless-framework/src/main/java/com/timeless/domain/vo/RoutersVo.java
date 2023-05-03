package com.timeless.domain.vo;

import com.timeless.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author timeless
 * @create 2022-12-16 0:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutersVo {

    private List<Menu> menus;

}
