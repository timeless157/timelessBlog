package com.timeless.controller;

import com.timeless.domain.ResponseResult;
import com.timeless.domain.entity.Article;
import com.timeless.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author timeless
 * @create 2022-12-05 14:22
 */

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        return articleService.hotArticleList();
    }

    @GetMapping("/articleList")
    public ResponseResult articleList(Long categoryId,Integer pageNum,Integer pageSize){
        return articleService.articleList(categoryId,pageNum,pageSize);
    }

    @GetMapping("/{id}")
    public ResponseResult gerArticleDetail(@PathVariable("id") Long id){
        return articleService.gerArticleDetail(id);
    }

    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }

}
