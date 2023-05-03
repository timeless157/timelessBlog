package com.timeless.controller;

import com.timeless.domain.ResponseResult;
import com.timeless.domain.entity.Link;
import com.timeless.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author timeless
 * @create 2022-12-07 10:39
 */
@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/getAllLink")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }

    @PostMapping
    public ResponseResult addLink(@RequestBody Link link) {
        linkService.save(link);
        return ResponseResult.okResult();
    }

}
