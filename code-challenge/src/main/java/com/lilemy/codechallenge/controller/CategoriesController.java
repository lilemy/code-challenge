package com.lilemy.codechallenge.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilemy.codechallenge.common.BaseResponse;
import com.lilemy.codechallenge.common.ResultCode;
import com.lilemy.codechallenge.common.ResultUtils;
import com.lilemy.codechallenge.exception.ThrowUtils;
import com.lilemy.codechallenge.model.dto.categories.CategoriesCreateRequest;
import com.lilemy.codechallenge.model.dto.categories.CategoriesQueryRequest;
import com.lilemy.codechallenge.model.entity.Categories;
import com.lilemy.codechallenge.model.vo.CategoriesVO;
import com.lilemy.codechallenge.service.CategoriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 笔记分类接口
 */
@Slf4j
@RestController
@RequestMapping("/categories")
@Tag(name = "CategoriesController")
public class CategoriesController {

    @Resource
    private CategoriesService categoriesService;

    @Operation(summary = "创建笔记分类")
    @PostMapping("/create")
    public BaseResponse<Long> createCategories(@RequestBody CategoriesCreateRequest categoriesCreateRequest) {
        ThrowUtils.throwIf(categoriesCreateRequest == null, ResultCode.PARAMS_ERROR);
        Categories categories = new Categories();
        BeanUtils.copyProperties(categoriesCreateRequest, categories);
        categoriesService.validCategories(categories, true);
        boolean save = categoriesService.save(categories);
        ThrowUtils.throwIf(!save, ResultCode.SUCCESS);
        return ResultUtils.success(categories.getId());
    }

    @Operation(summary = "分页获取笔记分类列表（封装类）")
    @PostMapping("/list/vo")
    public BaseResponse<Page<CategoriesVO>> listCategoriesVOByPage(@RequestBody CategoriesQueryRequest categoriesQueryRequest) {
        ThrowUtils.throwIf(categoriesQueryRequest == null, ResultCode.PARAMS_ERROR);
        long current = categoriesQueryRequest.getCurrent();
        long size = categoriesQueryRequest.getPageSize();
        // 查询数据库
        Page<Categories> categoriesPage = categoriesService.page(new Page<>(current, size),
                categoriesService.getQueryWrapper(categoriesQueryRequest));
        // 获取封装
        return ResultUtils.success(categoriesService.getCategoriesVOPage(categoriesPage));
    }
}
