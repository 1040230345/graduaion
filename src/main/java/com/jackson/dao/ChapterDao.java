package com.jackson.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ChapterDao {
    //查询docker镜像名称
    @Select("select dockerPath from chapter where id = #{chapterId}")
    String getDockerPathById(@Param("chapterId") Integer chapterId);
}
