package com.timeless.controller;

import com.timeless.domain.ResponseResult;
import com.timeless.domain.entity.Link;
import com.timeless.domain.vo.PageVo;
import com.timeless.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author timeless
 * @create 2022-12-18 2:05
 */
@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult listBackLink(Integer pageNum, Integer pageSize, String name, String status) {
        PageVo pageVo = linkService.listBackLink(pageNum, pageSize, name, status);
        return ResponseResult.okResult(pageVo);
    }

    @PostMapping
    public ResponseResult addLink(@RequestBody Link link) {
        linkService.save(link);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getLinkForUpdate(@PathVariable Long id) {
        return ResponseResult.okResult(linkService.getById(id));
    }

    @PutMapping
    public ResponseResult updateLink(@RequestBody Link link) {
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody Link link) {
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable Long id) {
        linkService.removeById(id);
        return ResponseResult.okResult();
    }

}
