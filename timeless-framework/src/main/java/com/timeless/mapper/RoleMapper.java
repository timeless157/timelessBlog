package com.timeless.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.timeless.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-15 16:15:52
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<String> selectRoleKeyByUserId(Long userId);
}
