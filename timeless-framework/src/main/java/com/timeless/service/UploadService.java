package com.timeless.service;

import com.timeless.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author timeless
 * @create 2022-12-12 22:53
 */
public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);

    ResponseResult uploadImgByMinIO(MultipartFile img);
}
