package com.timeless.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.timeless.domain.entity.User;
import com.timeless.domain.entity.UserVx;

/**
 * (SysUserVx)表服务接口
 *
 * @author makejava
 * @since 2023-04-22 17:38:41
 */
public interface UserVxService extends IService<UserVx> {
    User getUserByOpenId(String openid);
}
