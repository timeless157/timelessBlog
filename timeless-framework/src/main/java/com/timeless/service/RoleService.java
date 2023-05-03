package com.timeless.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.timeless.domain.dto.AddRoleDto;
import com.timeless.domain.dto.ChangeRoleStatusDto;
import com.timeless.domain.dto.UpdateRoleDto;
import com.timeless.domain.entity.Role;
import com.timeless.domain.vo.GetRoleDetailVo;
import com.timeless.domain.vo.PageVo;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-12-15 16:15:54
 */
public interface RoleService extends IService<Role> {
    List<String> selectRoleKeyByUserId(Long id);

    PageVo listBackRole(Integer pageNum, Integer pageSize, String roleName, String status);

    void changeStatus(ChangeRoleStatusDto changeRoleStatusDto);

    void addRole(AddRoleDto addRoleDto);

    GetRoleDetailVo getRoleDetail(Long id);

    void updateRole(UpdateRoleDto updateRoleVo);

    void deleteRole(Long id);

    List<Role> listAllRole();

}
