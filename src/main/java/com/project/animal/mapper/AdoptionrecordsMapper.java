package com.project.animal.mapper;

import com.project.animal.entity.Adoptionrecords;
import com.project.animal.entity.RecordDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * (Adoptionrecords)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-09 12:04:20
 */
@Mapper
public interface AdoptionrecordsMapper {

    @Select("select * from adoptionrecords where animalid = #{animalid}")
    Adoptionrecords queryByAnimalId(Integer animalid);

    @Select("select * from adoptionrecords where userid = #{userid}")
    List<Adoptionrecords> queryByUserId(Integer userid);

    @Select("select * from adoptionrecords "+
            "left join users on adoptionrecords.userid = users.userid " +
            "left join animals on adoptionrecords.animalid = animals.animalid ")
    List<RecordDTO> queryAll();

    @Insert("insert into adoptionrecords(animalid,userid,applicationdate,adoptiondate) " +
            "values(#{animalid},#{userid},#{applicationdate},now())")
    void addRecords(Adoptionrecords adoptionrecords);

    @Delete("delete from adoptionrecords where adoptionrecordid = #{recordId}")
    void delRecords(Integer recordId);

    @Update("update adoptionrecords set animalid=#{animalid},userid=#{userid}," +
            "applicationdate=#{applicationdate},adoptiondate=#{adoptiondate} " +
            "where adoptionrecordid=#{adoptionrecordid}")
    void updateRecords(Adoptionrecords adoptionrecords);

    @Select("select * from adoptionrecords where adoptionrecordid = #{recordId}")
    Adoptionrecords selectByRecordId(Integer recordId);
}

