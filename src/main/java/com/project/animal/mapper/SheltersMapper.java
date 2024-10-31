package com.project.animal.mapper;

import com.project.animal.entity.Shelters;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * (Shelters)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-15 11:07:05
 */
@Mapper
public interface SheltersMapper {

    @Select("select * from shelters")
    List<Shelters> getAllSh();


    @Select("select * from shelters where shelterid=#{shelterid}")
    Shelters getShById(Integer shelterid);


    @Insert("insert into shelters(sheltername,shelteraddress,shelterphone,introduce) " +
            "values(#{sheltername},#{shelteraddress},#{shelterphone},#{introduce})")
    void addSh(Shelters shelters);

    @Insert("delete from shelters where shelterid=#{shelterid}")
    void deleteSh(Integer shelterid);

    @Update("update shelters set sheltername=#{sheltername}," +
                    "shelteraddress=#{shelteraddress}," +
                    "shelterphone=#{shelterphone}," +
                    "introduce=#{introduce} where shelterid=#{shelterid}"
    )
    void updateSh(Shelters sh);


    @Select("select * from shelters where sheltername=#{sheltername}")
    Shelters getShByName(String name);
}

