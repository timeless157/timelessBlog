package com.timeless.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author timeless
 * @create 2022-12-07 12:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogUserLoginVo {

    private String token;
    private UserInfoVo userInfo;
}
