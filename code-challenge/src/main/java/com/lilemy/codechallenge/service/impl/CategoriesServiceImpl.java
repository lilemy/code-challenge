package com.lilemy.codechallenge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilemy.codechallenge.model.entity.Categories;
import com.lilemy.codechallenge.service.CategoriesService;
import com.lilemy.codechallenge.mapper.CategoriesMapper;
import org.springframework.stereotype.Service;

/**
* @author qq233
* @description 针对表【categories(笔记分类)】的数据库操作Service实现
*/
@Service
public class CategoriesServiceImpl extends ServiceImpl<CategoriesMapper, Categories>
    implements CategoriesService{

}




