package com.project.animal.service.impl;
import com.project.animal.entity.Carousel;
import com.project.animal.entity.CarouselDTO;
import org.springframework.stereotype.Service;
import com.project.animal.mapper.CarouselMapper;
import com.project.animal.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * (Carousel)表服务实现类
 *
 * @author makejava
 * @since 2024-04-30 00:39:16
 */
@Service
public class CarouselServiceImpl implements CarouselService {
   @Autowired
   private CarouselMapper carouselMapper;

   @Override
   public void update(Carousel carousel) {
      carouselMapper.update(carousel);
   }

   @Override
   public List<CarouselDTO> queryAll() {
      return carouselMapper.queryAll();
   }

   @Override
   public Carousel queryById(Integer activityId) {
      return carouselMapper.queryById(activityId);
   }
}
