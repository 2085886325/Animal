package com.project.animal.service.impl;
import com.project.animal.entity.Announcements;
import com.project.animal.entity.NoticeDTO;
import org.springframework.stereotype.Service;
import com.project.animal.mapper.AnnouncementsMapper;
import com.project.animal.service.AnnouncementsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * (Announcements)表服务实现类
 *
 * @author makejava
 * @since 2024-03-14 15:26:01
 */
@Service
public class AnnouncementsServiceImpl implements AnnouncementsService {
   @Autowired
   private AnnouncementsMapper announcementsMapper;

   @Override
   public NoticeDTO selectById(Integer announcementId) {
      return announcementsMapper.selectById(announcementId);
   }

   @Override
   public List<NoticeDTO> selectAll() {
      return announcementsMapper.selectAll();
   }

   @Override
   public List<Announcements> selectByStatus() {
      return announcementsMapper.selectByStatus();
   }

   @Override
   public void deleteAnnouncement(Integer announcementId) {
      announcementsMapper.deleteAnnouncement(announcementId);
   }

   @Override
   public void addAnnouncement(Announcements announcements) {
      announcementsMapper.addAnnouncement(announcements);
   }

   @Override
   public void updateAnnouncement(Announcements announcements) {
      announcementsMapper.updateAnnouncement(announcements);
   }
}
