package com.lilemy.codechallenge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilemy.codechallenge.model.entity.NoteFavour;
import com.lilemy.codechallenge.service.NoteFavourService;
import com.lilemy.codechallenge.mapper.NoteFavourMapper;
import org.springframework.stereotype.Service;

/**
* @author qq233
* @description 针对表【note_favour(笔记收藏)】的数据库操作Service实现
*/
@Service
public class NoteFavourServiceImpl extends ServiceImpl<NoteFavourMapper, NoteFavour>
    implements NoteFavourService{

}




