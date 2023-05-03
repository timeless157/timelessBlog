package com.timeless.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author timeless
 * @create 2022-12-07 12:31
 */
@Data
@Accessors(chain = true)
public class UserInfoVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    private String sex;

    private String email;


}
