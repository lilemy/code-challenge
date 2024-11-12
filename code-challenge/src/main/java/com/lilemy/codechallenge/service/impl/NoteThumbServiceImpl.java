package com.lilemy.codechallenge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilemy.codechallenge.model.entity.NoteThumb;
import com.lilemy.codechallenge.service.NoteThumbService;
import com.lilemy.codechallenge.mapper.NoteThumbMapper;
import org.springframework.stereotype.Service;

/**
* @author qq233
* @description 针对表【note_thumb(笔记点赞)】的数据库操作Service实现
*/
@Service
public class NoteThumbServiceImpl extends ServiceImpl<NoteThumbMapper, NoteThumb>
    implements NoteThumbService{

}




