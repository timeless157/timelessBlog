package com.timeless.service.impl;

import com.timeless.domain.ResponseResult;
import com.timeless.enums.AppHttpCodeEnum;
import com.timeless.exception.SystemException;
import com.timeless.service.UploadService;
import com.timeless.utils.OSSUtils;
import com.timeless.utils.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author timeless
 * @create 2022-12-12 22:54
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private OSSUtils ossUtils;

    @Override
    public ResponseResult uploadImg(MultipartFile img) {

        //获取原始文件名
        String originalFilename = img.getOriginalFilename();
        if (!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")) {
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        String url = ossUtils.uploadOSSImg(img, PathUtils.generateFilePath(originalFilename));

        return ResponseResult.okResult(url);
    }
}
