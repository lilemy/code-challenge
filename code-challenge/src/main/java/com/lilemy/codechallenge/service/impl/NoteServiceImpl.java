package com.lilemy.codechallenge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilemy.codechallenge.model.entity.Note;
import com.lilemy.codechallenge.service.NoteService;
import com.lilemy.codechallenge.mapper.NoteMapper;
import org.springframework.stereotype.Service;

/**
* @author qq233
* @description 针对表【note(笔记)】的数据库操作Service实现
*/
@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note>
    implements NoteService{

}




