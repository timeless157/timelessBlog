package com.timeless.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.timeless.domain.entity.Menu;
import com.timeless.domain.vo.MenuTreeSelectVo;
import com.timeless.domain.vo.RoleMenuTreeSelectVo;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2022-12-15 16:08:00
 */
public interface MenuService extends IService<Menu> {
    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<Menu> listBackMenu(String menuName, String status);

    void updateMenu(Menu menu);

    void deleteMenu(Long menuId);

    List<MenuTreeSelectVo> treeSelect();

    RoleMenuTreeSelectVo roleMenuTreeSelect(Long id);
}
