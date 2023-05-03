package com.timeless.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.dto.AddUserDto;
import com.timeless.domain.dto.UpdateUserDto;
import com.timeless.domain.entity.Role;
import com.timeless.domain.entity.User;
import com.timeless.domain.entity.UserRole;
import com.timeless.domain.vo.GetUserForUpdateVo;
import com.timeless.domain.vo.PageVo;
import com.timeless.domain.vo.RoleVo;
import com.timeless.domain.vo.UserInfoVo;
import com.timeless.enums.AppHttpCodeEnum;
import com.timeless.exception.SystemException;
import com.timeless.mapper.UserMapper;
import com.timeless.service.RoleService;
import com.timeless.service.UserRoleService;
import com.timeless.service.UserService;
import com.timeless.utils.BeanCopyUtils;
import com.timeless.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-12-09 18:00:55
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult userInfo() {

        Long userId = SecurityUtils.getUserId();
        User user = getById(userId);
        //转Vo
        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(vo);

    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();

    }

    @Override
    public ResponseResult register(User user) {

        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (emailExist(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //...
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public PageVo listBackUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.like(StringUtils.hasText(userName), User::getUserName, userName);
        userLambdaQueryWrapper.like(StringUtils.hasText(phonenumber), User::getPhonenumber, phonenumber);
        userLambdaQueryWrapper.like(StringUtils.hasText(status), User::getStatus, status);
        userLambdaQueryWrapper.orderByAsc(User::getId);
        Page<User> userPage = new Page<>(pageNum, pageSize);
        page(userPage, userLambdaQueryWrapper);
        return new PageVo(userPage.getRecords(), userPage.getTotal());
    }

    @Override
    @Transactional
    public void addUser(AddUserDto addUserDto) {
        //对数据进行非空判断
        if (!StringUtils.hasText(addUserDto.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if (userNameExist(addUserDto.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(addUserDto.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (emailExist(addUserDto.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(addUserDto.getPassword());
        addUserDto.setPassword(encodePassword);
        //转换，存入数据库
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        save(user);

        //user_role添加记录
        List<Long> roleIds = addUserDto.getRoleIds();
        ArrayList<UserRole> userRoles = new ArrayList<>();
        for (Long roleId : roleIds) {
            userRoles.add(new UserRole(user.getId(), roleId));
        }
        userRoleService.saveBatch(userRoles);
    }

    @Override
    @Transactional
    public void deleteUser(List<Long> id) {
        //删除user
        removeByIds(id);
        //删除user_role
        userRoleService.remove(new LambdaQueryWrapper<UserRole>().in(UserRole::getUserId, id));
    }

    @Override
    public GetUserForUpdateVo getUserForUpdate(Long id) {
        //查询roleIds
        List<UserRole> list = userRoleService.list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, id));
        ArrayList<Long> roleIds = new ArrayList<>();
        for (UserRole userRole : list) {
            roleIds.add(userRole.getRoleId());
        }
        //查询roles
        List<Role> roleList = roleService.list();
        //查询user，自己的信息
        User user = getById(id);
        return new GetUserForUpdateVo(roleIds, roleList, user);
    }

    @Override
    @Transactional
    public void updateUser(UpdateUserDto updateUserDto) {
        //更新user
        updateById(BeanCopyUtils.copyBean(updateUserDto, User.class));
        //更新user_role
        userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId,updateUserDto.getId()));
        ArrayList<UserRole> userRoles = new ArrayList<>();
        for (Long roleId : updateUserDto.getRoleIds()) {
            userRoles.add(new UserRole(updateUserDto.getId(), roleId));
        }
        userRoleService.saveBatch(userRoles);
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail, email);
        int count = count(userLambdaQueryWrapper);
        return count > 0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getNickName, nickName);
        int count = count(userLambdaQueryWrapper);
        return count > 0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName, userName);
        int count = count(userLambdaQueryWrapper);
        return count > 0;
    }
}
