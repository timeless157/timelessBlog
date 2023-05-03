package com.timeless.domain.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * (SysUserVx)表实体类
 *
 * @author makejava
 * @since 2023-04-22 17:38:40
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user_vx")
public class UserVx {

    private Long userId;

    private String openId;
}
