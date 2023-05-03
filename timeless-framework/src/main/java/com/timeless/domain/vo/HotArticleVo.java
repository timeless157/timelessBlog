package com.timeless.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author timeless
 * @create 2022-12-05 20:22
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotArticleVo {

    private Long id;
    //标题
    private String title;
    //访问量
    private Long viewCount;

}
