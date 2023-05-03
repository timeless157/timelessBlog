package com.timeless.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timeless.constants.SystemConstants;
import com.timeless.domain.dto.AddRoleDto;
import com.timeless.domain.dto.ChangeRoleStatusDto;
import com.timeless.domain.dto.UpdateRoleDto;
import com.timeless.domain.entity.*;
import com.timeless.domain.vo.GetRoleDetailVo;
import com.timeless.domain.vo.PageVo;
import com.timeless.domain.vo.RoleVo;
import com.timeless.mapper.RoleMapper;
import com.timeless.mapper.UserRoleMapper;
import com.timeless.service.RoleMenuService;
import com.timeless.service.RoleService;
import com.timeless.service.UserRoleService;
import com.timeless.service.UserService;
import com.timeless.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-12-15 16:15:54
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        ArrayList<String> roleKeys = new ArrayList<>();
        if (id == 1L) {
            roleKeys.add("admin");
            return roleKeys;
        }
        return getBaseMapper().selectRoleKeyByUserId(id);

    }

    @Override
    public PageVo listBackRole(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.like(StringUtils.hasText(roleName), Role::getRoleName, roleName);
        roleLambdaQueryWrapper.like(StringUtils.hasText(status), Role::getStatus, status);
        roleLambdaQueryWrapper.orderByAsc(Role::getRoleSort);
        Page<Role> rolePage = new Page<>(pageNum, pageSize);
        page(rolePage, roleLambdaQueryWrapper);
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(rolePage.getRecords(), RoleVo.class);
        return new PageVo(roleVos, rolePage.getTotal());
    }

    @Override
    public void changeStatus(ChangeRoleStatusDto changeRoleStatusDto) {
        update(new UpdateWrapper<Role>()
                .eq("id", changeRoleStatusDto.getRoleId())
                .set("status", changeRoleStatusDto.getStatus()));
    }

    @Override
    @Transactional
    public void addRole(AddRoleDto addRoleDto) {
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        save(role);
        ArrayList<RoleMenu> roleMenus = new ArrayList<>();
        for (int i = 0; i < addRoleDto.getMenuIds().size(); i++) {
            RoleMenu roleMenu = new RoleMenu(role.getId(), addRoleDto.getMenuIds().get(i));
            roleMenus.add(roleMenu);
        }
        roleMenuService.saveBatch(roleMenus);
    }

    @Override
    public GetRoleDetailVo getRoleDetail(Long id) {
        return BeanCopyUtils.copyBean(getById(id), GetRoleDetailVo.class);
    }

    @Override
    @Transactional
    public void updateRole(UpdateRoleDto updateRoleVo) {
        update(BeanCopyUtils.copyBean(updateRoleVo, Role.class), new UpdateWrapper<Role>().eq("id", updateRoleVo.getId()));
        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id", updateRoleVo.getId()));
        ArrayList<RoleMenu> roleMenus = new ArrayList<>();
        for (Long menuId : updateRoleVo.getMenuIds()) {
            roleMenus.add(new RoleMenu(updateRoleVo.getId(), menuId));
        }
        roleMenuService.saveBatch(roleMenus);
    }

    /**
     * 删除角色，同时也删除role_menu,user_role,user（太狠了，想想以后有没有更好的方式）
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteRole(Long id) {
        //删除role表
        update(new UpdateWrapper<Role>().eq("id", id)
                .set("del_flag", 1));
        //删除role_menu表
        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id", id));
        //删除user表,暂时不实现，后面再看看
//        List<UserRole> list = userRoleService.list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, id));
//        userService.update()
        //删除user_role表
        userRoleService.remove(new QueryWrapper<UserRole>().eq("role_id", id));
    }

    @Override
    public List<Role> listAllRole() {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getDelFlag, SystemConstants.ROLE_DEL_FLAG_ZERO_NORMAL);
        List<Role> list = list(roleLambdaQueryWrapper);
        return list;
    }
}
