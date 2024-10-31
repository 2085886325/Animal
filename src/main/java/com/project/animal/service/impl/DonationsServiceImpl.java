package com.project.animal.service.impl;
import com.project.animal.entity.Donations;
import org.springframework.stereotype.Service;
import com.project.animal.mapper.DonationsMapper;
import com.project.animal.service.DonationsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * (Donations)表服务实现类
 *
 * @author makejava
 * @since 2024-03-14 23:24:46
 */
@Service
public class DonationsServiceImpl implements DonationsService {
   @Autowired
   private DonationsMapper donationsMapper;

   @Override
   public List<Donations> selectAll() {
      return donationsMapper.selectAll();
   }

   @Override
   public List<Donations> selectByUserId(Integer userId) {
      return donationsMapper.selectByUserId(userId);
   }

   @Override
   public Donations selectByDoId(Integer donationid) {
      return donationsMapper.selectByDoId(donationid);
   }

   @Override
   public void addDonation(Donations donations) {
      donationsMapper.addDonation(donations);
   }

   @Override
   public void deleteDonation(Integer donationid) {
      donationsMapper.deleteDonation(donationid);
   }

   @Override
   public void updateDonation(Donations donations) {
      donationsMapper.updateDonation(donations);
   }
}
