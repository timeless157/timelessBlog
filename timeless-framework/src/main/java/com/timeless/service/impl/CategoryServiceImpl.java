package com.timeless.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timeless.constants.SystemConstants;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.dto.AddCategoryDto;
import com.timeless.domain.entity.Article;
import com.timeless.domain.entity.Category;
import com.timeless.domain.entity.User;
import com.timeless.domain.vo.CategoryVo;
import com.timeless.domain.vo.PageVo;
import com.timeless.mapper.CategoryMapper;
import com.timeless.service.ArticleService;
import com.timeless.service.CategoryService;
import com.timeless.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-12-05 21:23:26
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    /**
     * 查询文章分类列表
     * @return
     */
    @Override
    public ResponseResult getCategoryList() {
        //1.查询文章表，状态为0（已发布），的文章
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleLambdaQueryWrapper);
        //2.获取文章的分类id，并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        //3.查询分类表
        List<Category> categories = listByIds(categoryIds);

        categories = categories.stream()
                .filter(category -> SystemConstants.CATEGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //4.封装Vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(Category::getStatus,SystemConstants.CATEGORY_STATUS_NORMAL);
        List<Category> categoryList = list(categoryLambdaQueryWrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);
        return categoryVos;
    }

    @Override
    public PageVo listBackCategory(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.like(StringUtils.hasText(name), Category::getName, name);
        categoryLambdaQueryWrapper.like(StringUtils.hasText(status), Category::getStatus, status);
        Page<Category> categoryPage = new Page<>(pageNum, pageSize);
        page(categoryPage, categoryLambdaQueryWrapper);
        return new PageVo(categoryPage.getRecords(), categoryPage.getTotal());
    }

    @Override
    public void addCategory(AddCategoryDto addCategoryDto) {
        save(BeanCopyUtils.copyBean(addCategoryDto, Category.class));
    }
}
