package com.project.animal.service.impl;
import com.project.animal.entity.Adoptionrecords;
import com.project.animal.entity.RecordDTO;
import org.springframework.stereotype.Service;
import com.project.animal.mapper.AdoptionrecordsMapper;
import com.project.animal.service.AdoptionrecordsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * (Adoptionrecords)表服务实现类
 *
 * @author makejava
 * @since 2024-03-09 12:04:20
 */
@Service
public class AdoptionrecordsServiceImpl implements AdoptionrecordsService {
   @Autowired
   private AdoptionrecordsMapper adoptionrecordsMapper;

   @Override
   public Adoptionrecords queryByAnimalId(Integer animalid) {
      return adoptionrecordsMapper.queryByAnimalId(animalid);
   }

   @Override
   public List<Adoptionrecords> queryByUserId(Integer userid) {
      return adoptionrecordsMapper.queryByUserId(userid);
   }

   @Override
   public void updateRecords(Adoptionrecords adoptionrecords) {
      adoptionrecordsMapper.updateRecords(adoptionrecords);
   }

   @Override
   public Adoptionrecords selectByRecordId(Integer recordId) {
      return adoptionrecordsMapper.selectByRecordId(recordId);
   }

   @Override
   public List<RecordDTO> queryAll() {
      return adoptionrecordsMapper.queryAll();
   }


   @Override
   public void addRecords(Adoptionrecords adoptionrecords) {
      adoptionrecordsMapper.addRecords(adoptionrecords);
   }

   @Override
   public void delRecords(Integer recordId) {
      adoptionrecordsMapper.delRecords(recordId);
   }


}
