package com.timeless.handler.exception;

import com.timeless.domain.ResponseResult;
import com.timeless.enums.AppHttpCodeEnum;
import com.timeless.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author timeless
 * @create 2022-12-08 20:52
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        e.printStackTrace();
        log.error(e.getMessage());
        return ResponseResult.errorResult(e.getCode(),e.getMessage());
    }

//    @ExceptionHandler(InsufficientAuthenticationException.class)
//    public ResponseResult insufficientAuthenticationExceptionHandler(InsufficientAuthenticationException e){
//        return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN.getCode(),"需要登陆");
//    }

    /**
     * 处理其他异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){
        e.printStackTrace();
        log.error(e.getMessage());
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }

}
