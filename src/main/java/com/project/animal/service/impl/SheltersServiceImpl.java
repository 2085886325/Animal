package com.project.animal.service.impl;
import com.project.animal.entity.Shelters;
import org.springframework.stereotype.Service;
import com.project.animal.mapper.SheltersMapper;
import com.project.animal.service.SheltersService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * (Shelters)表服务实现类
 *
 * @author makejava
 * @since 2024-03-15 11:07:05
 */
@Service
public class SheltersServiceImpl implements SheltersService {
   @Autowired
   private SheltersMapper sheltersMapper;

   @Override
   public List<Shelters> getAllSh() {
      return sheltersMapper.getAllSh();
   }

   @Override
   public Shelters getShById(Integer shelterid) {
      return sheltersMapper.getShById(shelterid);
   }

   @Override
   public void addSh(Shelters sh) {
      sheltersMapper.addSh(sh);
   }

   @Override
   public void deleteSh(Integer shelterid) {
      sheltersMapper.deleteSh(shelterid);
   }

   @Override
   public void updateSh(Shelters sh) {
      sheltersMapper.updateSh(sh);
   }

   @Override
   public Shelters getShByName(String name) {
      return sheltersMapper.getShByName(name);
   }
}
