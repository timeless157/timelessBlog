package com.timeless.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.dto.AddArticleDto;
import com.timeless.domain.entity.Article;
import com.timeless.domain.vo.ArticleForUpdateVo;
import com.timeless.domain.vo.PageVo;

import java.util.List;

/**
 * @author timeless
 * @create 2022-12-05 14:19
 */
public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Long categoryId, Integer pageNum, Integer pageSize);

    ResponseResult gerArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    void addArticle(AddArticleDto addArticleDto);

    PageVo listArticles(Integer pageNum, Integer pageSize, String title, String summary);

    ArticleForUpdateVo getArticleForUpdate(Long id);


//    void updateArticle(ArticleForUpdateVo articleForUpdateVo);

    void deleteArticleById(List<Long> id);

    void updateArticle(ArticleForUpdateVo articleForUpdateVo);
}
