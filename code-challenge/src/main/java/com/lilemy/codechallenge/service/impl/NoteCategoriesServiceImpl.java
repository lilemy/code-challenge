package com.lilemy.codechallenge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilemy.codechallenge.model.entity.NoteCategories;
import com.lilemy.codechallenge.service.NoteCategoriesService;
import com.lilemy.codechallenge.mapper.NoteCategoriesMapper;
import org.springframework.stereotype.Service;

/**
* @author qq233
* @description 针对表【note_categories(笔记分类关系)】的数据库操作Service实现
*/
@Service
public class NoteCategoriesServiceImpl extends ServiceImpl<NoteCategoriesMapper, NoteCategories>
    implements NoteCategoriesService{

}




