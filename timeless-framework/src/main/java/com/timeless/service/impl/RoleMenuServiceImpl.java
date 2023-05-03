package com.timeless.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timeless.domain.entity.RoleMenu;
import com.timeless.mapper.RoleMenuMapper;
import com.timeless.service.RoleMenuService;
import org.springframework.stereotype.Service;
/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2022-12-17 18:49:55
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
}
