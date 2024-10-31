package com.project.animal.service;


import com.project.animal.entity.Adoptapply;
import com.project.animal.entity.AdoptapplyDTO;

import java.util.List;

/**
 * (Adoptapply)表服务接口
 *
 * @author makejava
 * @since 2024-03-13 15:51:27
 */
public interface AdoptapplyService {

    //添加时候需要通过这个判断是否已经被申请了
    Adoptapply selectByAnimalId(Integer animalid);
    //通过申请id查询
    Adoptapply selectByApplyId(Integer applicationid);

    List<AdoptapplyDTO> selectByUserId(Integer userId);

    void addApply(Adoptapply adoptapply);

    List<AdoptapplyDTO> selectAll();

    void deleteApply(Integer applicationid);

    void updateApply(Adoptapply adoptapply);

    //修改userId和animalId还有status
    boolean updateAdoptStatus(Integer applicationid,String status, Integer animalid, Integer userid);
}

