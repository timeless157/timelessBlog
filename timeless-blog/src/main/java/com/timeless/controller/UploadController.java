package com.timeless.controller;

import com.timeless.domain.ResponseResult;
import com.timeless.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author timeless
 * @create 2022-12-12 22:51
 */
@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img){
        return uploadService.uploadImg(img);
    }

    /**
     * 使用MinIO存储用户头像
     * @param img
     * @return
     */
    @PostMapping(value = "/uploadByMinIO",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseResult uploadByMinIO(@RequestPart("img") MultipartFile img){
        return uploadService.uploadImgByMinIO(img);
    }

}
