package com.timeless.service.impl;

import com.timeless.domain.ResponseResult;
import com.timeless.domain.entity.LoginUser;
import com.timeless.domain.entity.User;
import com.timeless.domain.vo.BlogUserLoginVo;
import com.timeless.domain.vo.UserInfoVo;
import com.timeless.service.AdminLoginService;
import com.timeless.service.BlogLoginService;
import com.timeless.utils.BeanCopyUtils;
import com.timeless.utils.JwtUtil;
import com.timeless.utils.RedisCache;
import com.timeless.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author timeless
 * @create 2022-12-07 11:42
 */
@Service
public class AdminLoginServiceImpl implements AdminLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误!");
        }
        //获取userId生成Token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject("adminlogin:" + userId, loginUser);
        //把token封装 返回
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        //1.获取userId
        Long userId = SecurityUtils.getUserId();
        //3.清除redis登录信息
        redisCache.deleteObject("adminlogin:" + userId);

        return ResponseResult.okResult();
    }

}
