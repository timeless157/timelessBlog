package com.timeless.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.timeless.constants.SystemConstants;
import com.timeless.domain.entity.LoginUser;
import com.timeless.domain.entity.User;
import com.timeless.enums.AppHttpCodeEnum;
import com.timeless.mapper.UserMapper;
import com.timeless.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author timeless
 * @create 2022-12-07 11:59
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //1.根据用户名查询用户信息
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName, s);
        User user = userMapper.selectOne(userLambdaQueryWrapper);
        //2.未查询到用户，抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //3.返回用户信息
        if(user.getStatus().equals("1")){
            throw new RuntimeException("用户已停用");
        }

        if(user.getType().equals(SystemConstants.ADMIN)){
            List<String> perms = menuService.selectPermsByUserId(user.getId());
            return new LoginUser(user,perms);
        }
        return new LoginUser(user,null);

    }
}
