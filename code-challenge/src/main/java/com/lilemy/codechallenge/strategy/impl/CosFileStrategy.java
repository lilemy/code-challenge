package com.lilemy.codechallenge.strategy.impl;

import com.lilemy.codechallenge.common.ResultCode;
import com.lilemy.codechallenge.config.CosClientConfig;
import com.lilemy.codechallenge.exception.BusinessException;
import com.lilemy.codechallenge.strategy.FileStrategy;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 腾讯云对象存储
 */
@Slf4j
public class CosFileStrategy implements FileStrategy {

    /**
     * COS 访问地址
     */
    private static final String COS_HOST = "https://code-challenge-1327171061.cos.ap-shanghai.myqcloud.com";

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    @Override
    public String uploadFile(MultipartFile multipartFile, String filePath) {
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filePath, null);
            multipartFile.transferTo(file);
            // 上传文件
            PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), filePath,
                    file);
            cosClient.putObject(putObjectRequest);
            // 返回可访问地址
            return COS_HOST + filePath;
        } catch (Exception e) {
            log.error("cos file upload error, filePath = {}", filePath, e);
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filePath = {}", filePath);
                }
            }
        }
    }
}
