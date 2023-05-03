package com.timeless.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timeless.constants.SystemConstants;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.dto.AddArticleDto;
import com.timeless.domain.entity.Article;
import com.timeless.domain.entity.ArticleTag;
import com.timeless.domain.entity.Category;
import com.timeless.domain.entity.Tag;
import com.timeless.domain.vo.*;
import com.timeless.mapper.ArticleMapper;
import com.timeless.service.ArticleService;
import com.timeless.service.ArticleTagService;
import com.timeless.service.CategoryService;
import com.timeless.service.TagService;
import com.timeless.utils.BeanCopyUtils;
import com.timeless.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author timeless
 * @create 2022-12-05 14:20
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;


    /**
     * 查询显示浏览量前十的文章
     *
     * @return
     */
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //正式文章，浏览量从高到低排序，最多10篇
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        lambdaQueryWrapper.orderByDesc(Article::getViewCount);
        Page<Article> page = new Page<>(SystemConstants.HOT_ARTICLE_DEFAULT_BEGIN, SystemConstants.HOT_ARTICLE_DEFAULT_ALL);

        page(page, lambdaQueryWrapper);
        //拿到page里面所有的记录条
        List<Article> articles = page.getRecords();
        for (int i = 0; i < articles.size(); i++) {
            Integer viewCountRedis = redisCache.getCacheMapValue("article:viewCount", articles.get(i).getId().toString());
            articles.get(i).setViewCount(viewCountRedis.longValue());
        }
        //bean拷贝
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);
    }

    /**
     * 查询文章，首页和分类。
     *
     * @param categoryId 类别
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult articleList(Long categoryId, Integer pageNum, Integer pageSize) {

        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //1.查询条件:有分类的id
        articleLambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        //2.查询条件：正式发布的文章
        articleLambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //3. 根据isTop降序，再根据创建时间降序
        articleLambdaQueryWrapper.orderBy(true, true, Article::getIsTop)
                .orderBy(true, false, Article::getCreateTime);
        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, articleLambdaQueryWrapper);

        //获取分类id，查询分类信息，得到分类名称,分类名称设置给article
        List<Article> articles = page.getRecords();
        articles = articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

        for (int i = 0; i < articles.size(); i++) {
            Integer viewCountRedis = redisCache.getCacheMapValue("article:viewCount", articles.get(i).getId().toString());
            articles.get(i).setViewCount(viewCountRedis.longValue());
        }

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 获取文章详情
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult gerArticleDetail(Long id) {
        //1.根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //2.转换成Vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //3.根据分类id查询分类名
        articleDetailVo.setCategoryName(categoryService.getById(articleDetailVo.getCategoryId()).getName());

        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue("article:viewCount", id.toString(), 1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public void addArticle(AddArticleDto addArticleDto) {
        //添加博客
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        save(article);

        //博客标签表增加记录
        List<ArticleTag> articleTags = addArticleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
    }

    @Override
    public PageVo listArticles(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.like(StringUtils.hasText(title), Article::getTitle, title);
        articleLambdaQueryWrapper.like(StringUtils.hasText(summary), Article::getSummary, summary);
        articleLambdaQueryWrapper.orderByAsc(Article::getId);
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        page(articlePage, articleLambdaQueryWrapper);
        List<ArticleBackListVo> articleBackListVos = BeanCopyUtils.copyBeanList(articlePage.getRecords(), ArticleBackListVo.class);

        return new PageVo(articleBackListVos, articlePage.getTotal());
    }

    @Override
    public ArticleForUpdateVo getArticleForUpdate(Long id) {
        Article article = getById(id);
        ArticleForUpdateVo articleForUpdateVo = BeanCopyUtils.copyBean(article, ArticleForUpdateVo.class);

        //查询分类list
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, id);
        List<ArticleTag> list = articleTagService.list(articleTagLambdaQueryWrapper);
        ArrayList<Long> tagIds = new ArrayList<>();
        for (ArticleTag articleTag : list) {
            tagIds.add(articleTag.getTagId());
        }
        articleForUpdateVo.setTags(tagIds);
        return articleForUpdateVo;
    }

    @Override
    @Transactional
    public void updateArticle(ArticleForUpdateVo articleForUpdateVo) {
//        redisCache.setCacheMapValue("article:viewCount" , articleForUpdateVo.getId().toString(),articleForUpdateVo.getViewCount());
        HashMap<String, Integer> viewCountMap = new HashMap<>();
        viewCountMap.put(articleForUpdateVo.getId().toString() , articleForUpdateVo.getViewCount().intValue());
        redisCache.setCacheMap("article:viewCount", viewCountMap);
        updateById(BeanCopyUtils.copyBean(articleForUpdateVo, Article.class));
        //更改article_tag表
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,articleForUpdateVo.getId());
        articleTagService.remove(articleTagLambdaQueryWrapper);

        List tags = articleForUpdateVo.getTags();
        ArrayList<ArticleTag> articleTags = new ArrayList<>();
        for (int i = 0; i < tags.size(); i++) {
            articleTags.add(new ArticleTag(articleForUpdateVo.getId(), Long.valueOf(tags.get(i).toString())));
        }
        articleTagService.saveBatch(articleTags);
    }

    @Override
    public void deleteArticleById(List<Long> id) {
        update(new UpdateWrapper<Article>().in("id",id).set("del_flag",1));
    }


}
