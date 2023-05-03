package com.timeless.controller;

import com.timeless.domain.ResponseResult;
import com.timeless.domain.dto.AddRoleDto;
import com.timeless.domain.dto.ChangeRoleStatusDto;
import com.timeless.domain.entity.Role;
import com.timeless.domain.vo.GetRoleDetailVo;
import com.timeless.domain.vo.PageVo;
import com.timeless.domain.dto.UpdateRoleDto;
import com.timeless.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author timeless
 * @create 2022-12-17 13:14
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult listBackRole(Integer pageNum,Integer pageSize,String roleName,String status){
        PageVo list = roleService.listBackRole(pageNum,pageSize,roleName,status);
        return ResponseResult.okResult(list);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeRoleStatusDto changeRoleStatusDto){
        roleService.changeStatus(changeRoleStatusDto);
        return ResponseResult.okResult();
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        roleService.addRole(addRoleDto);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getRoleDetail(@PathVariable Long id){
        GetRoleDetailVo roleDetailVo = roleService.getRoleDetail(id);
        return ResponseResult.okResult(roleDetailVo);
    }

    @PutMapping
    public ResponseResult updateRole(@RequestBody UpdateRoleDto updateRoleVo){
        roleService.updateRole(updateRoleVo);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable Long id){
        roleService.deleteRole(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        List<Role> roles = roleService.listAllRole();
        return ResponseResult.okResult(roles);
    }

}
