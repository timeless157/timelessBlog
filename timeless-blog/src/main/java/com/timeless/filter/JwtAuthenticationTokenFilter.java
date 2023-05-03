package com.timeless.filter;

import com.alibaba.fastjson.JSON;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.entity.LoginUser;
import com.timeless.enums.AppHttpCodeEnum;
import com.timeless.utils.JwtUtil;
import com.timeless.utils.RedisCache;
import com.timeless.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author timeless
 * @create 2022-12-08 12:49
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //1.获取请求头中的token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //说明该接口不需要登陆，直接放行;
            //个人感觉这里只是为了放行登录接口，因为就算访问了需要token的接口，没有token后面也会拦截的
            filterChain.doFilter(request, response);
            return;
        }
        //2.解析获取userId
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token超时、非法
            //响应告诉前端需要重新登录
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(responseResult));
            return;
        }
        String userId = claims.getSubject();
        //3.从redis中获取用户信息，因为登陆的时候存再redis里面了。
        LoginUser loginUser = redisCache.getCacheObject("blogLogin:" + userId);
        //如果获取不到，说明登陆过期
        if (Objects.isNull(loginUser)) {
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(responseResult));
            return;
        }
        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
