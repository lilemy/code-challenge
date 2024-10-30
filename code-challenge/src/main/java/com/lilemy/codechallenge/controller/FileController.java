package com.lilemy.codechallenge.controller;

import com.lilemy.codechallenge.common.BaseResponse;
import com.lilemy.codechallenge.common.ResultUtils;
import com.lilemy.codechallenge.manager.MinioManager;
import com.lilemy.codechallenge.model.dto.file.UploadFileRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private MinioManager minioManager;

    @Operation(summary = "文件上传")
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestParam("file") MultipartFile file,UploadFileRequest uploadFileRequest) {
        String url = minioManager.uploadFile(file, uploadFileRequest);
        return ResultUtils.success(url);
    }
}
