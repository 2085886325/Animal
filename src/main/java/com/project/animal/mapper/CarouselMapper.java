package com.project.animal.mapper;

import com.project.animal.entity.Carousel;
import com.project.animal.entity.CarouselDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * (Carousel)表数据库访问层
 *
 * @author makejava
 * @since 2024-04-30 00:39:16
 */
@Mapper
public interface CarouselMapper {

    @Update("update carousel set image_url = #{imageUrl},activity_id = #{activityId} where carousel_id = #{carouselId}")
    void update(Carousel carousel);

    @Select("select c.*,activities.title from carousel c left join activities on c.activity_id = activities.activity_id")
    List<CarouselDTO> queryAll();

    @Select("select * from carousel where activity_id = #{activityId}")
    Carousel queryById(Integer activityId);
}

