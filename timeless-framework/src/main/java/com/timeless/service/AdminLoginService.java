package com.timeless.service;

import com.timeless.domain.ResponseResult;
import com.timeless.domain.entity.User;

/**
 * @author timeless
 * @create 2022-12-07 11:42
 */
public interface AdminLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
