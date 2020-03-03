package com.jackson.dao;

import com.jackson.exception.CustomizeErrorCode;
import com.jackson.model.Curriculum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CurriculumDao {
    /**
     * 获取课程列表总数
     * @return
     */
    @Select("select count(*) from curriculum t ")
    Long countAllCurriculum();

    @Select("select * from curriculum t order by t.id limit #{startPosition},#{limit}")
    List<Curriculum> getCurriculumList(@Param("startPosition")Integer startPosition, @Param("limit")Integer limit);
}
