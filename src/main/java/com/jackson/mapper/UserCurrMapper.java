package com.jackson.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jackson.model.Curriculum;
import com.jackson.model.UserCurr;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserCurrMapper extends BaseMapper<UserCurr> {

    @Select("select c.* from curriculum c " +
            "join user_curr uc on c.id = uc.curr_id " +
            "where uc.user_name = #{username} ")
    Curriculum getUserCurr(@Param("username") String username);

    /**
     * 删除自己做过的实验
     */
    @Delete("delete from user_curr where username = #{username} and curr_id = #{userCurrId}")
    Integer delUserCurr(@Param("username") String username,
                        @Param("userCurrId") Integer userCurrId);

}
