package com.lilemy.codechallenge.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilemy.codechallenge.common.ResultCode;
import com.lilemy.codechallenge.constant.CommonConstant;
import com.lilemy.codechallenge.exception.ThrowUtils;
import com.lilemy.codechallenge.mapper.CategoriesMapper;
import com.lilemy.codechallenge.model.dto.categories.CategoriesQueryRequest;
import com.lilemy.codechallenge.model.entity.Categories;
import com.lilemy.codechallenge.model.vo.CategoriesVO;
import com.lilemy.codechallenge.service.CategoriesService;
import com.lilemy.codechallenge.util.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qq233
 * @description 针对表【categories(笔记分类)】的数据库操作Service实现
 */
@Service
public class CategoriesServiceImpl extends ServiceImpl<CategoriesMapper, Categories>
        implements CategoriesService {

    @Override
    public void validCategories(Categories categories, boolean add) {
        ThrowUtils.throwIf(categories == null, ResultCode.PARAMS_ERROR);
        // 从对象中取值
        String name = categories.getName();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(name), ResultCode.PARAMS_ERROR, "分类名称不能为空");
        }
        // 修改数据时，有参数则校验
        if (StringUtils.isNotBlank(name)) {
            ThrowUtils.throwIf(name.length() > 20, ResultCode.PARAMS_ERROR, "分类名称过长");
        }
    }

    @Override
    public QueryWrapper<Categories> getQueryWrapper(CategoriesQueryRequest categoriesQueryRequest) {
        QueryWrapper<Categories> queryWrapper = new QueryWrapper<>();
        if (categoriesQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = categoriesQueryRequest.getId();
        Long notId = categoriesQueryRequest.getNotId();
        String name = categoriesQueryRequest.getName();
        String sortField = categoriesQueryRequest.getSortField();
        String sortOrder = categoriesQueryRequest.getSortOrder();
        String underlineSortField = StrUtil.toUnderlineCase(sortField);
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                underlineSortField);
        return queryWrapper;
    }

    @Override
    public Page<CategoriesVO> getCategoriesVOPage(Page<Categories> categoriesPage) {
        List<Categories> categoriesList = categoriesPage.getRecords();
        Page<CategoriesVO> categoriesVOPage = new Page<>(categoriesPage.getCurrent(), categoriesPage.getSize());
        if (CollUtil.isEmpty(categoriesList)) {
            return categoriesVOPage;
        }
        List<CategoriesVO> categoriesVOList = categoriesList.stream().map(CategoriesVO::objToVo).toList();
        categoriesVOPage.setRecords(categoriesVOList);
        return categoriesVOPage;
    }
}




