package com.timeless.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timeless.domain.entity.UserRole;
import com.timeless.mapper.UserRoleMapper;
import com.timeless.service.UserRoleService;
import org.springframework.stereotype.Service;
/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2022-12-17 22:00:15
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
