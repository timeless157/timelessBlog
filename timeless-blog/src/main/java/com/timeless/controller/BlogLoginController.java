package com.timeless.controller;

import com.timeless.domain.ResponseResult;
import com.timeless.domain.dto.LoginByQQEmailDto;
import com.timeless.domain.entity.User;
import com.timeless.enums.AppHttpCodeEnum;
import com.timeless.exception.SystemException;
import com.timeless.service.BlogLoginService;
import com.timeless.utils.QQEmailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author timeless
 * @create 2022-12-07 11:40
 */

@RestController
@Slf4j
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    @Autowired
    private QQEmailUtils qqEmailUtils;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
//        log.error("控制器");
        if(!StringUtils.hasText(user.getUserName())){
            //提示必须传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    @PostMapping("/loginByQQEmail")
    public ResponseResult loginByQQEmail(@RequestBody LoginByQQEmailDto loginByQQEmailDto){
//        log.error("控制器");
        if(!StringUtils.hasText(loginByQQEmailDto.getEmail())){
            //提示必须传邮箱
            throw new SystemException(AppHttpCodeEnum.REQUIRE_EMAIL);
        }
        return blogLoginService.loginByQQEmail(loginByQQEmailDto);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }

    @GetMapping("/sendCode")
    public ResponseResult sendCode(@RequestParam("email") String email){
        qqEmailUtils.sendCode(email);
        return ResponseResult.okResult();
    }

}
