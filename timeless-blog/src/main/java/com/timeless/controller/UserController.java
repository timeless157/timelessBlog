package com.timeless.controller;

import com.timeless.annotation.SystemLog;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.entity.User;
import com.timeless.domain.vo.UserInfoVo;
import com.timeless.service.UserService;
import com.timeless.utils.BeanCopyUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author timeless
 * @create 2022-12-12 20:45
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    public ResponseResult userInfo(@RequestParam("userId") Long userId) {
        //微信登陆
        if (userService.getById(userId) != null) {
            UserInfoVo vo = BeanCopyUtils.copyBean(userService.getById(userId), UserInfoVo.class);
//            System.out.println("啊啊啊啊啊啊啊啊");
//            System.out.println(vo);
            return ResponseResult.okResult(vo);
        }
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    @SystemLog(businessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user) {
        return userService.register(user);
    }

}
