package com.lilemy.codechallenge.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lilemy.codechallenge.model.dto.note.*;
import com.lilemy.codechallenge.model.entity.Note;
import com.lilemy.codechallenge.model.vo.NoteVO;

/**
 * @author qq233
 * @description 针对表【note(笔记)】的数据库操作Service
 */
public interface NoteService extends IService<Note> {

    /**
     * 校验数据
     *
     * @param note 笔记信息
     * @param add  对创建的数据进行校验
     */
    void validQuestion(Note note, boolean add);

    /**
     * 创建笔记
     *
     * @param noteCreateRequest 笔记创建请求体
     * @return 笔记 id
     */
    Long createNote(NoteCreateRequest noteCreateRequest);

    /**
     * 删除笔记
     *
     * @param noteId 笔记 id
     * @return 是否操作成功
     */
    Boolean deleteNote(Long noteId);

    /**
     * 编辑笔记
     *
     * @param noteEditRequest 编辑笔记请求体
     * @return 是否编辑成功
     */
    Boolean editNote(NoteEditRequest noteEditRequest);

    /**
     * 更新笔记
     *
     * @param noteUpdateRequest 更新笔记请求体
     * @return 是否更新成功
     */
    Boolean updateNote(NoteUpdateRequest noteUpdateRequest);

    /**
     * 获取笔记封装
     *
     * @param note 笔记信息
     * @return 笔记信息封装
     */
    NoteVO getNoteVO(Note note);

    /**
     * 构建笔记查询条件
     *
     * @param noteQueryRequest 笔记查询请求体
     * @return 笔记查询条件
     */
    QueryWrapper<Note> getQueryWrapper(NoteQueryRequest noteQueryRequest);

    /**
     * 构建笔记查询条件（已审核笔记）
     *
     * @param noteQueryRequest 笔记查询请求体
     * @return 笔记查询条件
     */
    QueryWrapper<Note> getReviewQueryWrapper(NoteQueryRequest noteQueryRequest);

    /**
     * 分页获取笔记信息封装
     *
     * @param notePage 笔记信息分页
     * @return 笔记封装信息分页
     */
    Page<NoteVO> getNoteVOPage(Page<Note> notePage);

    /**
     * 笔记审核
     *
     * @param noteReviewRequest 笔记审核请求体
     * @return 是否操作成功
     */
    boolean reviewNote(NoteReviewRequest noteReviewRequest);

}
