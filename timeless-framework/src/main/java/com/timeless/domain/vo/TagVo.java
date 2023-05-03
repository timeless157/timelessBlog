package com.timeless.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author timeless
 * @create 2022-12-16 14:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagVo {

    private Long id;
    //标签名
    private String name;

}
