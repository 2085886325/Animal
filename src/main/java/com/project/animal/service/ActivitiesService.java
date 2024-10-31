package com.project.animal.service;


import com.project.animal.entity.Activities;
import com.project.animal.entity.ActivityDTO;

import java.util.List;

/**
 * (Activities)表服务接口
 *
 * @author makejava
 * @since 2024-04-30 00:38:45
 */
public interface ActivitiesService {

    List<ActivityDTO> queryAll();

    Activities queryById(Integer activityId);

    void insert(Activities activities);

    void update(Activities activities);

    void deleteById(Integer activityId);
}

