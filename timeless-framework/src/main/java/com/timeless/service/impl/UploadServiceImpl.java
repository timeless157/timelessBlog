package com.timeless.service.impl;

import com.timeless.domain.ResponseResult;
import com.timeless.enums.AppHttpCodeEnum;
import com.timeless.exception.SystemException;
import com.timeless.service.UploadService;
import com.timeless.utils.OSSUtils;
import com.timeless.utils.PathUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author timeless
 * @create 2022-12-12 22:54
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private OSSUtils ossUtils;

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.urlPrefix}")
    private String urlPrefix;

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

    @Override
    public ResponseResult uploadImgByMinIO(MultipartFile img) {
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();
        if (!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")) {
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        String pictureName = PathUtils.generateFilePath(originalFilename);

        try {
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(bucket)
                            .object(pictureName)
                            .stream(img.getInputStream(),img.getInputStream().available(),-1)
                            .contentType(img.getContentType())
                            .build());
        } catch (Exception e) {
            throw new SystemException(AppHttpCodeEnum.UPLOAD_FAIL);
        }
        String url = urlPrefix + bucket + "/" + pictureName;
//        String url = "http://localhost:9000/timelessbloguserpicture/" + pictureName;
        return ResponseResult.okResult(url);

    }
}
