package com.lilemy.codechallenge.controller;

import com.lilemy.codechallenge.common.BaseResponse;
import com.lilemy.codechallenge.common.ResultUtils;
import com.lilemy.codechallenge.model.dto.file.UploadFileRequest;
import com.lilemy.codechallenge.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "FileController")
public class FileController {

    @Resource
    private FileService fileService;

    @Operation(summary = "文件上传")
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestParam("file") MultipartFile file, UploadFileRequest uploadFileRequest) {
        String url = fileService.uploadFile(file, uploadFileRequest);
        return ResultUtils.success(url);
    }
}
