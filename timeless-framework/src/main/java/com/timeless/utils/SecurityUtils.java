package com.timeless.utils;

import com.timeless.domain.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author timeless
 * @create 2022-12-09 23:09
 */
public class SecurityUtils
{

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Boolean isAdmin(){
        Long id = getLoginUser().getUser().getId();
        return id != null && id.equals(1L);
    }

    public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }
}
