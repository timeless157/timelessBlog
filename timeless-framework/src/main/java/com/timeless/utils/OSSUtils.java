package com.timeless.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author timeless
 * @create 2022-12-12 22:56
 */
@Component
@ConfigurationProperties(prefix = "oss")
@Data
public class OSSUtils {

    private String accessKey;
    private String secretKey;
    private String bucket;
    private String domainName;

    public String uploadOSSImg(MultipartFile imgFile,String filePath) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());

        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;

        try {
            InputStream is = imgFile.getInputStream();
            //...生成上传凭证，然后准备上传
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(is, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                return domainName + key;
            } catch (QiniuException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return key;
    }


}
