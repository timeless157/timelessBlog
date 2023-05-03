package com.timeless.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timeless.constants.SystemConstants;
import com.timeless.domain.entity.Menu;
import com.timeless.domain.entity.RoleMenu;
import com.timeless.domain.vo.MenuTreeSelectVo;
import com.timeless.domain.vo.RoleMenuTreeSelectVo;
import com.timeless.enums.AppHttpCodeEnum;
import com.timeless.exception.SystemException;
import com.timeless.mapper.MenuMapper;
import com.timeless.service.MenuService;
import com.timeless.service.RoleMenuService;
import com.timeless.utils.BeanCopyUtils;
import com.timeless.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-12-15 16:08:00
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 根据用户id查询对应的权限信息
     *
     * @param id
     * @return
     */
    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是超级管理员返回所有权限
        if (id.equals(1L)) {
            LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            menuLambdaQueryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            menuLambdaQueryWrapper.eq(Menu::getStatus, SystemConstants.MENU_STATUS_NORMAL);
            List<Menu> menuList = list(menuLambdaQueryWrapper);
            List<String> perms = menuList.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus;
        //如果是超级管理员返回所有权限
        if (SecurityUtils.isAdmin()) {
            menus = menuMapper.selectAllRouterMenu();
        } else {
            //否则返回具有的权限
            menus = menuMapper.selectRouterTreeMenuByUserId(userId);
        }
        //构建树
        List<Menu> menuTree = buildMenuTree(menus, 0L);
        return menuTree;
    }

    @Override
    public List<Menu> listBackMenu(String menuName, String status) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.like(StringUtils.hasText(menuName), Menu::getMenuName, menuName);
        menuLambdaQueryWrapper.like(!ObjectUtils.isEmpty(status), Menu::getStatus, status);
        menuLambdaQueryWrapper.orderBy(true, true, Menu::getParentId, Menu::getOrderNum);
        List<Menu> list = list(menuLambdaQueryWrapper);
        return list;
    }

    @Override
    public void updateMenu(Menu menu) {
        if (menu.getId().equals(menu.getParentId())) {
            throw new SystemException(AppHttpCodeEnum.SON_CANNOT_BE_FATHER);
        }
        updateById(menu);
    }

    @Override
    @Transactional
    public void deleteMenu(Long menuId) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(Menu::getParentId, menuId);
        List<Menu> list = list(menuLambdaQueryWrapper);
        if (!list.isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.HAVE_SON_MENU);
        }
        update(new UpdateWrapper<Menu>().eq("id", menuId).set("del_flag", "1"));
    }

    /**
     * Done 前端参数menu的menu_name需要替代label
     * @return
     */
    @Override
    public List<MenuTreeSelectVo> treeSelect() {

        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(Menu::getStatus, SystemConstants.MENU_STATUS_NORMAL);
        menuLambdaQueryWrapper.orderByAsc(Menu::getOrderNum);
        menuLambdaQueryWrapper.orderByAsc(Menu::getParentId);
        List<Menu> menuList = list(menuLambdaQueryWrapper);

        List<Menu> menus = buildMenuTree(menuList, 0L);

        List<MenuTreeSelectVo> menuTreeSelectVos = BeanCopyUtils.copyBeanList(menus, MenuTreeSelectVo.class);
        //因为子菜单的属性不对应，所以需要设置MenuTreeSelectVo的孩子

        for (int i = 0; i < menuTreeSelectVos.size(); i++) {
            menuTreeSelectVos.get(i).setChildren(BeanCopyUtils.copyBeanList(menus.get(i).getChildren(), MenuTreeSelectVo.class));
//            menuTreeSelectVos.get(i).setLabel("我是主菜单" + i);
            for (int j = 0; j < menuTreeSelectVos.get(i).getChildren().size(); j++) {
                menuTreeSelectVos.get(i)
                        .getChildren().get(j)
                        .setChildren(BeanCopyUtils.copyBeanList(menus.get(i).getChildren().get(j).getChildren(), MenuTreeSelectVo.class));
//                menuTreeSelectVos.get(i).getChildren().get(j).setLabel("我是次菜单" + i + j);
                for (int k = 0; k < menuTreeSelectVos.get(i).getChildren().get(j).getChildren().size(); k++) {
                    menuTreeSelectVos.get(i).getChildren()
                            .get(j).getChildren()
                            .get(k)
                            .setChildren(BeanCopyUtils.copyBeanList
                                    (menus.get(i).getChildren().get(j).getChildren().get(k).getChildren(), MenuTreeSelectVo.class));
//                    menuTreeSelectVos.get(i).getChildren().get(j).getChildren().get(k).setLabel("我是次次菜单" + i + j + k);
                }
            }
        }
        return menuTreeSelectVos;
    }

    @Override
    public RoleMenuTreeSelectVo roleMenuTreeSelect(Long id) {
        //获取菜单树
        List<MenuTreeSelectVo> menuTreeSelectVos = treeSelect();

        LambdaQueryWrapper<RoleMenu> roleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuLambdaQueryWrapper.eq(RoleMenu::getRoleId,id);
        List<RoleMenu> menus = roleMenuService.list(roleMenuLambdaQueryWrapper);

        ArrayList<Long> menuIdsChecked = new ArrayList<>();
        for (RoleMenu menu : menus) {
            menuIdsChecked.add(menu.getMenuId());
        }

        return new RoleMenuTreeSelectVo(menuTreeSelectVos,menuIdsChecked);
    }


    private List<Menu> buildMenuTree(List<Menu> menus, long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(menu1 -> menu1.getParentId().equals(menu.getId()))
                .map(menu1 -> menu1.setChildren(getChildren(menu1, menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}
