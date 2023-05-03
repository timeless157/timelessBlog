package com.timeless.handler.security;

import com.alibaba.fastjson.JSON;
import com.timeless.domain.ResponseResult;
import com.timeless.enums.AppHttpCodeEnum;
import com.timeless.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author timeless
 * @create 2022-12-08 13:43
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        e.printStackTrace();
        //响应给前端
        ResponseResult responseResult = null;
        if (e instanceof BadCredentialsException) {
            responseResult = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(), e.getMessage());
        } else if(e instanceof InsufficientAuthenticationException) {
            responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }else{
            responseResult = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"认证或授权失败");
        }
//        System.out.println(responseResult);
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(responseResult));
    }
}
