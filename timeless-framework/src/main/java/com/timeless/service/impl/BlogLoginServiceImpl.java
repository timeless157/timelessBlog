package com.timeless.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.dto.LoginByQQEmailDto;
import com.timeless.domain.entity.LoginUser;
import com.timeless.domain.entity.User;
import com.timeless.domain.vo.BlogUserLoginVo;
import com.timeless.domain.vo.UserInfoVo;
import com.timeless.enums.AppHttpCodeEnum;
import com.timeless.exception.SystemException;
import com.timeless.service.BlogLoginService;
import com.timeless.service.UserService;
import com.timeless.utils.BeanCopyUtils;
import com.timeless.utils.JwtUtil;
import com.timeless.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @author timeless
 * @create 2022-12-07 11:42
 */
@Service
@Slf4j
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult login(User user) {
//        log.error("服务层");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
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
        redisCache.setCacheObject("blogLogin:" + userId, loginUser);
        //把token和userInfo封装，返回
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt, BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class));
        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult logout() {
        //1.获取token解析获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //2.获取userId
        Long userId = loginUser.getUser().getId();
        //3.清除redis登录信息
        redisCache.deleteObject("blogLogin:" + userId);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult loginByQQEmail(LoginByQQEmailDto loginByQQEmailDto) {

        //判断邮箱是否存在
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail, loginByQQEmailDto.getEmail());
        User user = userService.getOne(userLambdaQueryWrapper);

        if (ObjectUtils.isEmpty(user)) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_IS_NOT_EXIST);
        }
        //存在，去redis中获取验证码
        String code = redisCache.getCacheObject("blogLogin:" + loginByQQEmailDto.getEmail());
        //验证code是否正确
        if (!StringUtils.hasText(code)) {
            throw new SystemException(AppHttpCodeEnum.CODE_IS_EXPIRED);
        }
        //验证码正确，删掉redis中的验证码,查出来用户信息返回
        if (code.equals(loginByQQEmailDto.getCode())) {
            //查用户信息封装
            redisCache.setCacheObject("blogLogin:" + user.getId(), new LoginUser(user , null));
            String jwt = JwtUtil.createJWT(user.getId().toString());
            BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt, BeanCopyUtils.copyBean(user, UserInfoVo.class));
            redisCache.deleteObject("blogLogin:" + loginByQQEmailDto.getEmail());
            return ResponseResult.okResult(blogUserLoginVo);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.CODE_ERROR);
    }
}
