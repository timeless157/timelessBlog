package com.timeless.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.dto.AddCategoryDto;
import com.timeless.domain.entity.Category;
import com.timeless.domain.vo.CategoryVo;
import com.timeless.domain.vo.PageVo;

import java.util.List;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-12-05 21:23:25
 */
public interface CategoryService extends IService<Category> {
    ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();

    PageVo listBackCategory(Integer pageNum, Integer pageSize, String name, String status);

    void addCategory(AddCategoryDto addCategoryDto);
}
