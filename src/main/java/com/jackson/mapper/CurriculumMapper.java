package com.jackson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jackson.model.Curriculum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CurriculumMapper extends BaseMapper<Curriculum> {
    /**
     * 获取课程列表总数
     * @return
     */
    @Select("select count(*) from curriculum t ")
    Long countAllCurriculum();

    @Select("select * from curriculum t order by t.id limit #{startPosition},#{limit}")
    List<Curriculum> getCurriculumList(@Param("startPosition")Integer startPosition, @Param("limit")Integer limit);

    @Select("select docker_path from curriculum t where id =#{id}")
    String getDockerPath(@Param("id") Integer id);

    @Select("select * from curriculum t where user_id = #{userId}")
    List<Curriculum> getUserCurriculum(@Param("userId") Integer userId);
}
