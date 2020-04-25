package com.jackson.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ChapterMapper {
    //查询docker镜像名称
    @Select("select docker_path from chapter where id = #{chapterId}")
    String getDockerPathById(@Param("chapterId") Integer chapterId);
}
