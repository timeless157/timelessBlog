package com.timeless.handler.security;

import com.alibaba.fastjson.JSON;
import com.timeless.domain.ResponseResult;
import com.timeless.enums.AppHttpCodeEnum;
import com.timeless.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author timeless
 * @create 2022-12-08 13:46
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        e.printStackTrace();
        //响应给前端
        ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(responseResult));
    }
}
