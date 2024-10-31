package com.project.animal.service;


import com.project.animal.entity.Announcements;
import com.project.animal.entity.NoticeDTO;

import java.util.List;

/**
 * (Announcements)表服务接口
 *
 * @author makejava
 * @since 2024-03-14 15:26:01
 */
public interface AnnouncementsService {

    NoticeDTO selectById(Integer announcementId);

    List<NoticeDTO> selectAll();

    List<Announcements> selectByStatus();

    void deleteAnnouncement(Integer announcementId);

    void addAnnouncement(Announcements announcements);


    void updateAnnouncement(Announcements announcements);
}

