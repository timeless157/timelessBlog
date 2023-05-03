package com.timeless.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timeless.domain.entity.User;
import com.timeless.domain.entity.UserVx;
import com.timeless.mapper.UserVxMapper;
import com.timeless.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.timeless.service.UserVxService;

/**
 * (SysUserVx)表服务实现类
 *
 * @author makejava
 * @since 2023-04-22 17:38:41
 */
@Service("sysUserVxService")
public class UserVxServiceImpl extends ServiceImpl<UserVxMapper, UserVx> implements UserVxService {

    @Autowired
    private UserService userService;
    @Override
    public User getUserByOpenId(String openid) {
        LambdaQueryWrapper<UserVx> userVxLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userVxLambdaQueryWrapper.eq(UserVx::getOpenId , openid);
        UserVx userVx = getOne(userVxLambdaQueryWrapper);
        if(userVx == null){
            return null;
        }
        User user = userService.getById(userVx.getUserId());
        if(user == null){
            return null;
        }
        return user;
    }
}
