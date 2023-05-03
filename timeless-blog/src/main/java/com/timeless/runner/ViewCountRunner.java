package com.timeless.runner;

import com.timeless.domain.entity.Article;
import com.timeless.mapper.ArticleMapper;
import com.timeless.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 程序启动时执行的任务
 *
 * @author timeless
 * @create 2022-12-14 12:22
 */
@Component
public class ViewCountRunner implements CommandLineRunner {

    @Resource
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {

        //查询博客信息 id viewCount
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream().collect(Collectors.toMap(article -> article.getId().toString(), article -> {
            return article.getViewCount().intValue();
        }));

        //存入redis中
        redisCache.setCacheMap("article:viewCount", viewCountMap);

    }
}
