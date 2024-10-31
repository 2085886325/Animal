package com.project.animal.service;


import com.project.animal.entity.Adoptionrecords;
import com.project.animal.entity.RecordDTO;

import java.util.List;

/**
 * (Adoptionrecords)表服务接口
 *
 * @author makejava
 * @since 2024-03-09 12:04:20
 */
public interface AdoptionrecordsService {

    Adoptionrecords queryByAnimalId(Integer animalid);

    List<RecordDTO> queryAll();

    void addRecords(Adoptionrecords adoptionrecords);

    void delRecords(Integer recordId);

    List<Adoptionrecords> queryByUserId(Integer userid);

    void updateRecords(Adoptionrecords adoptionrecords);

    Adoptionrecords selectByRecordId(Integer recordId);
}

