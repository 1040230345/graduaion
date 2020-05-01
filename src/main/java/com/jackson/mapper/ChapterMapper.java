package com.jackson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jackson.model.Chapter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ChapterMapper extends BaseMapper<Chapter> {

    /**
     * 获取实验内容
     */
    @Select("select text from chapter where id = #{chapterId} ")
    String getText(@Param("chapterId") Integer chapterId);

}
