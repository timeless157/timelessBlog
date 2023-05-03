package com.timeless.controller;

import com.timeless.domain.ResponseResult;
import com.timeless.domain.dto.AddArticleDto;
import com.timeless.domain.vo.ArticleForUpdateVo;
import com.timeless.domain.vo.PageVo;
import com.timeless.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author timeless
 * @create 2022-12-16 16:59
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult addArticle(@RequestBody AddArticleDto addArticleDto){
        articleService.addArticle(addArticleDto);
        return ResponseResult.okResult();
    }

    @GetMapping("/list")
    public ResponseResult listArticles(Integer pageNum,Integer pageSize,String title,String summary){
        PageVo list = articleService.listArticles(pageNum,pageSize,title,summary);
        return ResponseResult.okResult(list);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleForUpdate(@PathVariable Long id){
        ArticleForUpdateVo articleForUpdateVo = articleService.getArticleForUpdate(id);
        return ResponseResult.okResult(articleForUpdateVo);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody ArticleForUpdateVo articleForUpdateVo){
        articleService.updateArticle(articleForUpdateVo);
        return  ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticleById(@PathVariable List<Long> id){
        articleService.deleteArticleById(id);
        return ResponseResult.okResult();
    }

}
