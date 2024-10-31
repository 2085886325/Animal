package com.project.animal.service.impl;
import com.project.animal.entity.Adoptapply;
import com.project.animal.entity.AdoptapplyDTO;
import org.springframework.stereotype.Service;
import com.project.animal.mapper.AdoptapplyMapper;
import com.project.animal.service.AdoptapplyService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * (Adoptapply)表服务实现类
 *
 * @author makejava
 * @since 2024-03-13 15:51:27
 */
@Service
public class AdoptapplyServiceImpl implements AdoptapplyService {
   @Autowired
   private AdoptapplyMapper adoptapplyMapper;

   @Override
   public Adoptapply selectByAnimalId(Integer animalid) {
      Adoptapply adoptapply=adoptapplyMapper.selectApply(animalid);
      return adoptapply;
   }

   @Override
   public Adoptapply selectByApplyId(Integer applicationid) {
      return adoptapplyMapper.selectByApplyId(applicationid);
   }

   @Override
   public List<AdoptapplyDTO> selectByUserId(Integer userId) {
      return adoptapplyMapper.selectByUserId(userId);
   }

   @Override
   public void addApply(Adoptapply adoptapply) {
      adoptapplyMapper.addApply(adoptapply);
   }

   @Override
   public List<AdoptapplyDTO> selectAll() {
      return adoptapplyMapper.selectAll();
   }

   @Override
   public void deleteApply(Integer applicationid) {
      adoptapplyMapper.deleteApply(applicationid);
   }

   @Override
   public void updateApply(Adoptapply adoptapply) {
      adoptapplyMapper.updateApply(adoptapply);
   }

   @Override
   public boolean updateAdoptStatus(Integer applicationid,String status, Integer animalid, Integer userid) {
      return adoptapplyMapper.updateAdoptStatus(applicationid,status,animalid,userid);
   }

}
