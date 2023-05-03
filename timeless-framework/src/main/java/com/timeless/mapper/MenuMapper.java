package com.timeless.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.timeless.domain.entity.Menu;

import java.util.List;

/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-15 16:07:58
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(Long userId);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterTreeMenuByUserId(Long userId);
}
