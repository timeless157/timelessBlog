package com.timeless.controller;

import com.timeless.domain.ResponseResult;
import com.timeless.domain.entity.LoginUser;
import com.timeless.domain.entity.Menu;
import com.timeless.domain.entity.User;
import com.timeless.domain.vo.AdminUserInfoVo;
import com.timeless.domain.vo.RoutersVo;
import com.timeless.domain.vo.UserInfoVo;
import com.timeless.enums.AppHttpCodeEnum;
import com.timeless.exception.SystemException;
import com.timeless.service.AdminLoginService;
import com.timeless.service.BlogLoginService;
import com.timeless.service.MenuService;
import com.timeless.service.RoleService;
import com.timeless.utils.BeanCopyUtils;
import com.timeless.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author timeless
 * @create 2022-12-07 11:40
 */

@RestController
public class AdminLoginController {

    @Autowired
    private AdminLoginService adminLoginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示必须传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return adminLoginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> permsList = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //封装数据返回
        UserInfoVo userInfo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(permsList,roleKeyList,userInfo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        //查询menu，以tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(SecurityUtils.getUserId());
        //封装数据
        return ResponseResult.okResult(new RoutersVo(menus));

    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return adminLoginService.logout();
    }


}
