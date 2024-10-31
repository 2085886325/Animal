package com.project.animal.service;


import com.project.animal.entity.Carousel;
import com.project.animal.entity.CarouselDTO;

import java.util.List;

/**
 * (Carousel)表服务接口
 *
 * @author makejava
 * @since 2024-04-30 00:39:16
 */
public interface CarouselService {

    void update(Carousel carousel);

    List<CarouselDTO> queryAll();

    Carousel queryById(Integer activityId);
}

