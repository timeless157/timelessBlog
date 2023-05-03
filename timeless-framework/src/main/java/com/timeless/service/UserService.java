package com.timeless.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.dto.AddUserDto;
import com.timeless.domain.dto.UpdateUserDto;
import com.timeless.domain.entity.User;
import com.timeless.domain.vo.GetUserForUpdateVo;
import com.timeless.domain.vo.PageVo;

import java.util.List;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-12-09 18:00:54
 */
public interface UserService extends IService<User> {
    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    PageVo listBackUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    void addUser(AddUserDto addUserDto);

    void deleteUser(List<Long> id);

    GetUserForUpdateVo getUserForUpdate(Long id);

    void updateUser(UpdateUserDto updateUserDto);
}
