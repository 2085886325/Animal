package com.project.animal.service;


import com.project.animal.entity.Donations;

import java.util.List;

/**
 * (Donations)表服务接口
 *
 * @author makejava
 * @since 2024-03-14 23:24:46
 */
public interface DonationsService {

    List<Donations> selectAll();

    List<Donations> selectByUserId(Integer userid);

    Donations selectByDoId(Integer donationid);

    void addDonation(Donations donations);

    void deleteDonation(Integer donationid);

    void updateDonation(Donations donations);
}

