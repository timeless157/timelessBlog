package com.timeless.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author timeless
 * @create 2022-12-05 23:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleListVo {

    private Long id;
    //标题
    private String title;
    //文章摘要
    private String summary;
    //分类Id
    private Long categoryId;
    //所属分类名
    private String categoryName;
    //缩略图
    private String thumbnail;
    //访问量
    private Long viewCount;

    private Date createTime;

}
