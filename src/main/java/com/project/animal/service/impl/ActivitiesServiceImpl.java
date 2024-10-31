package com.project.animal.service.impl;
import com.project.animal.entity.Activities;
import com.project.animal.entity.ActivityDTO;
import org.springframework.stereotype.Service;
import com.project.animal.mapper.ActivitiesMapper;
import com.project.animal.service.ActivitiesService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * (Activities)表服务实现类
 *
 * @author makejava
 * @since 2024-04-30 00:38:45
 */
@Service
public class ActivitiesServiceImpl implements ActivitiesService {
   @Autowired
   private ActivitiesMapper activitiesMapper;

   @Override
   public List<ActivityDTO> queryAll() {
      return activitiesMapper.queryAll();
   }

   @Override
   public Activities queryById(Integer activityId) {
      return activitiesMapper.queryById(activityId);
   }

   @Override
   public void insert(Activities activities) {
      activitiesMapper.insert(activities);
   }

   @Override
   public void update(Activities activities) {
      activitiesMapper.update(activities);
   }

   @Override
   public void deleteById(Integer activityId) {
      activitiesMapper.deleteById(activityId);
   }
}
