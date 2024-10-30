package com.lilemy.codechallenge.manager;

import com.lilemy.codechallenge.common.ResultCode;
import com.lilemy.codechallenge.config.MinioConfig;
import com.lilemy.codechallenge.exception.BusinessException;
import com.lilemy.codechallenge.exception.ThrowUtils;
import com.lilemy.codechallenge.model.dto.file.UploadFileRequest;
import com.lilemy.codechallenge.model.enums.FileUploadBizEnum;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
public class MinioManager {

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinioConfig minioConfig;

    /**
     * 上传文件
     *
     * @param file 上传的文件
     * @return 文件访问 url
     */
    public String uploadFile(MultipartFile file, UploadFileRequest uploadFileRequest) {
        // 判断文件是否为空
        ThrowUtils.throwIf(file == null || file.getSize() == 0, ResultCode.PARAMS_ERROR, "文件不能为空");
        // 文件的原始名称
        String originalFileName = file.getOriginalFilename();
        ThrowUtils.throwIf(originalFileName == null, ResultCode.PARAMS_ERROR, "文件名字不能为空");
        // 获取业务信息
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        ThrowUtils.throwIf(fileUploadBizEnum == null || file.getSize() == 0, ResultCode.PARAMS_ERROR, "未知的业务类型");
        Long bizId = uploadFileRequest.getBizId();
        ThrowUtils.throwIf(bizId == null || bizId <= 0, ResultCode.PARAMS_ERROR, "业务 id 不存在");
        // 文件的 Content-Type
        String contentType = file.getContentType();
        // 生成存储对象的名称
        String uuid = RandomStringUtils.randomAlphanumeric(32);
        // 获取文件的后缀，如 .jpg
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 拼接上文件后缀，即为要存储的文件名
        String objectName = String.format("%s%s", uuid, suffix);
        String filePath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), bizId, objectName);
        try {
            // 上传文件至 Minio
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(filePath)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "上传失败");
        }
        // 返回文件的访问链接
        return String.format("%s/%s%s", minioConfig.getEndpoint(), minioConfig.getBucketName(), filePath);
    }
}
