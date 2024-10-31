package com.project.animal.service;


import com.project.animal.entity.Shelters;

import java.util.List;

/**
 * (Shelters)表服务接口
 *
 * @author makejava
 * @since 2024-03-15 11:07:05
 */
public interface SheltersService {

    List<Shelters> getAllSh();


    Shelters getShById(Integer shelterid);

    void addSh(Shelters sh);

    void deleteSh(Integer shelterid);

    void updateSh(Shelters sh);

    Shelters getShByName(String name);
}

