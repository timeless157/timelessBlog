package com.timeless.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.dto.AddCategoryDto;
import com.timeless.domain.entity.Category;
import com.timeless.domain.vo.CategoryVo;
import com.timeless.domain.vo.ExcelCategoryVo;
import com.timeless.domain.vo.PageVo;
import com.timeless.enums.AppHttpCodeEnum;
import com.timeless.exception.SystemException;
import com.timeless.service.CategoryService;
import com.timeless.utils.BeanCopyUtils;
import com.timeless.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author timeless
 * @create 2022-12-16 16:23
 */
@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory() {
        List<CategoryVo> list = categoryService.listAllCategory();
        return ResponseResult.okResult(list);
    }

//    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader(response);
            //获取需要导出的数据
            List<Category> category = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(category, ExcelCategoryVo.class);

            //数据写入Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            //出现异常，响应json
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(responseResult));
        }
    }

    @GetMapping("/list")
    public ResponseResult listBackCategory(Integer pageNum, Integer pageSize, String name, String status) {
        PageVo pageVo = categoryService.listBackCategory(pageNum, pageSize, name, status);
        return ResponseResult.okResult(pageVo);
    }

    @PostMapping
    public ResponseResult addCategory(@RequestBody AddCategoryDto addCategoryDto){
        categoryService.addCategory(addCategoryDto);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getCategoryForUpdate(@PathVariable Long id){
        Category category = categoryService.getById(id);
        return ResponseResult.okResult(category);
    }

    @PutMapping
    public ResponseResult updateCategory(@RequestBody Category category){
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable List<Long> id){
        categoryService.removeByIds(id);
        return ResponseResult.okResult();
    }

}
