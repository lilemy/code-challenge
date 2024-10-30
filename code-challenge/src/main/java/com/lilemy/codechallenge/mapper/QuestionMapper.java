package com.lilemy.codechallenge.mapper;

import com.lilemy.codechallenge.model.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
* @author qq233
* @description 针对表【question(题目)】的数据库操作Mapper
* @Entity com.lilemy.codechallenge.model.entity.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 查询题目列表（包括已被删除的数据）
     */
    @Select("select * from question where update_time >= #{minUpdateTime}")
    List<Question> listQuestionWithDelete(Date minUpdateTime);

}




