package com.timeless.job;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.timeless.domain.entity.Article;
import com.timeless.service.ArticleService;
import com.timeless.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author timeless
 * @create 2022-12-14 13:28
 */
@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "* 0/10 * * * ?")
    public void updateViewCount() {

        //获取redis浏览量
        Map<String, Integer> cacheMap = redisCache.getCacheMap("article:viewCount");
        //更新数据库
        List<Article> articles = cacheMap.entrySet().stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
//        articleService.updateBatchById(articles);
        for (Article article : articles) {
            LambdaUpdateWrapper<Article> articleLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            articleLambdaUpdateWrapper.eq(Article::getId, article.getId());
            articleLambdaUpdateWrapper.set(Article::getViewCount, article.getViewCount());
            articleService.update(articleLambdaUpdateWrapper);
        }

    }

}
