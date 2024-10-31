package com.project.animal.mapper;

import com.project.animal.entity.Activities;
import com.project.animal.entity.ActivityDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * (Activities)表数据库访问层
 *
 * @author makejava
 * @since 2024-04-30 00:38:45
 */
@Mapper
public interface ActivitiesMapper {

    @Select("select * from activities "+
            "left join users on activities.leader_id = users.userid ")
    List<ActivityDTO> queryAll();


    @Select("select * from activities where activity_id = #{activityId}")
    Activities queryById(Integer activityId);


    @Insert("INSERT INTO activities (title, description, leader_id, start_time, end_time, type, location, status)" +
            "VALUES (#{title}, #{description}, #{leaderId}, #{startTime}, #{endTime}, #{type}, #{location}, #{status});")
    void insert(Activities activities);


    @Update("UPDATE activities SET title = #{title}, description = #{description}, leader_id = #{leaderId}, start_time = #{startTime}, end_time = #{endTime}, type = #{type}, location = #{location}, views = #{views}, status = #{status}, created_at = now() WHERE activity_id = #{activityId}")
    void update(Activities activities);

    @Delete("delete from activities where activity_id = #{activityId}")
    void deleteById(Integer activityId);
}

