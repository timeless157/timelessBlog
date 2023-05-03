package com.timeless.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author timeless
 * @create 2022-12-05 23:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVo {

    private List rows;

    private Long total;

}
