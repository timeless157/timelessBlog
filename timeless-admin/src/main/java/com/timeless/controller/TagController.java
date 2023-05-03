package com.timeless.controller;

import com.timeless.domain.ResponseResult;
import com.timeless.domain.dto.UpdateTagDto;
import com.timeless.domain.entity.Tag;
import com.timeless.domain.vo.PageVo;
import com.timeless.domain.vo.TagVo;
import com.timeless.service.TagService;
import com.timeless.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author timeless
 * @create 2022-12-15 14:36
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(String name, Integer pageNum, Integer pageSize) {
        ResponseResult tagList = tagService.tagList(name, pageNum, pageSize);
        return tagList;
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody Tag tag) {
        tagService.save(tag);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable List<Long> id) {
        tagService.deleteById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult GetTagById(@PathVariable Long id) {
        return ResponseResult.okResult(tagService.getById(id));
    }

    @PutMapping
    public ResponseResult updateTag(@RequestBody UpdateTagDto updateTagDto) {
        Tag tag = BeanCopyUtils.copyBean(updateTagDto, Tag.class);
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }

}
