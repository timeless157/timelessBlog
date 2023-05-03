package com.timeless.controller;

import com.timeless.domain.ResponseResult;
import com.timeless.domain.dto.AddUserDto;
import com.timeless.domain.dto.UpdateUserDto;
import com.timeless.domain.entity.User;
import com.timeless.domain.vo.GetUserForUpdateVo;
import com.timeless.domain.vo.PageVo;
import com.timeless.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author timeless
 * @create 2022-12-17 22:46
 */
@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult listBackUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        PageVo list = userService.listBackUser(pageNum, pageSize, userName, phonenumber, status);
        return ResponseResult.okResult(list);
    }

    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto) {
        userService.addUser(addUserDto);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable List<Long> id) {
        userService.deleteUser(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getUserForUpdate(@PathVariable Long id) {
        GetUserForUpdateVo getUserForUpdateVo = userService.getUserForUpdate(id);
        return ResponseResult.okResult(getUserForUpdateVo);
    }

    @PutMapping
    public ResponseResult updateUser(@RequestBody UpdateUserDto updateUserDto) {
        userService.updateUser(updateUserDto);
        return ResponseResult.okResult();
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody User user){
        userService.updateById(user);
        return ResponseResult.okResult();
    }

}
