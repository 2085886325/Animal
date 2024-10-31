package com.project.animal.mapper;

import com.project.animal.entity.Adoptapply;
import com.project.animal.entity.AdoptapplyDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * (Adoptapply)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-13 15:51:27
 */
@Mapper
public interface AdoptapplyMapper {

    @Select("select * from adoptapply where animalid = #{animalid} and status!='2' ")
    Adoptapply selectApply(Integer animalid);

    @Select("select * from adoptapply where applicationid = #{applicationid}")
    Adoptapply selectByApplyId(Integer applicationid);

    @Select("select * from adoptapply  left join users on adoptapply.userid = users.userid " +
            "left join animals on adoptapply.animalid = animals.animalid " +
            "where adoptapply.userid = #{userId}")
    List<AdoptapplyDTO> selectByUserId(Integer userId);

    //查询所有申请，并联表查询用户名和动物名
    @Select("select * from adoptapply " +
            "left join users on adoptapply.userid = users.userid " +
            "left join animals on adoptapply.animalid = animals.animalid ")
    List<AdoptapplyDTO> selectAll();

    @Insert("insert into adoptapply(animalid, userid,applicationdate, status,adoptionmotivation," +
            "familyenvironment,personalexperience,additionalinformation) " +
            "values(#{animalid}, #{userid},now(),#{status},#{adoptionmotivation},#{familyenvironment}," +
            "#{personalexperience},#{additionalinformation})")
    void addApply(Adoptapply adoptapply);

    @Delete("delete from adoptapply where applicationid = #{applicationid}")
    void deleteApply(Integer applicationid);

    @Update("UPDATE adoptapply SET " +
            "animalid = #{animalid}, " +
            "userid = #{userid}, " +
            "applicationdate = #{applicationdate}, " +
            "status = #{status}, " +
            "adoptionmotivation = #{adoptionmotivation}," +
            "familyenvironment = #{familyenvironment}, " +
            "personalexperience = #{personalexperience}, " +
            "additionalinformation = #{additionalinformation}, " +
            "rejectionreason = #{rejectionreason} " +
            "where applicationid = #{applicationid}")
    void updateApply(Adoptapply adoptapply);

    @Update("update adoptapply set status = #{status},animalid = #{animalid},userid = #{userid} where applicationid = #{applicationid}")
    boolean updateAdoptStatus(Integer applicationid,String status, Integer animalid, Integer userid);
}

