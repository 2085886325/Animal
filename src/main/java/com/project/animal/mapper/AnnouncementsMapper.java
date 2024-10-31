package com.project.animal.mapper;

import com.project.animal.entity.Announcements;
import com.project.animal.entity.NoticeDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * (Announcements)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-14 15:26:01
 */
@Mapper
public interface AnnouncementsMapper {

    @Select("select * from announcements  left join users on announcements.publisher_id = users.userid " +
            "where announcement_id = #{announcementId}")
    NoticeDTO selectById(Integer announcementId);

    @Select("select * from announcements " +
            "left join users on announcements.publisher_id = users.userid ")
    List<NoticeDTO> selectAll();


    @Select("select * from announcements where status = 'published' ORDER BY announcement_id DESC; ")
    List<Announcements> selectByStatus();

    @Delete("delete from announcements where announcement_id = #{announcementId}")
    void deleteAnnouncement(Integer announcementId);

    @Insert("insert into announcements(title, content, publish_date, publisher_id, status) " +
            "values(#{title}, #{content}, now(), #{publisherId}, #{status})")
    void addAnnouncement(Announcements announcements);


    @Update("update announcements set title = #{title}, " +
            "content = #{content}, publish_date = #{publishDate}, " +
            "publisher_id = #{publisherId}, status = #{status} " +
            "where announcement_id = #{announcementId}")
    void updateAnnouncement(Announcements announcements);
}

