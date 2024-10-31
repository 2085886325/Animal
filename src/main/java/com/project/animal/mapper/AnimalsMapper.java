package com.project.animal.mapper;

import com.project.animal.entity.Animals;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author rhy
 * @since 2024-03-04
 */
@Mapper
public interface AnimalsMapper {

    @Select("select * from animals")
    List<Animals> list();

    @Select("select * from animals where adopterid=#{adopterid}")
    List<Animals> listByAdopterId(Integer adopterid);

    @Select("select * from animals where speciesid=#{speciesid}")
    List<Animals> listBySpeciesId(Integer speciesid);

    @Select("select * from animals where shelterid=#{shelterid}")
    List<Animals> listByShelterId(Integer shelterid);

    //根据动物id查询是否已经领养
    @Select("select * from animals where animalid=#{animalid}")
    Animals findAnimalById(Integer animalid);

    //0未领养
    //1已领养
    //2待批准
    @Insert("insert into animals(speciesid,name,gender,age,shelterid,description,weight,healthstatus,picture,status,adopterid) " +
            "values (#{speciesid},#{name},#{gender},#{age},#{shelterid},#{description},#{weight},#{healthstatus},#{picture},#{status},#{adopterid})")
    void addAnimal(Animals animals);

    @Update("update animals set speciesid=#{speciesid},name=#{name},description=#{description}," +
            "gender=#{gender},age=#{age},weight=#{weight},healthstatus=#{healthstatus}," +
            "picture=#{picture},shelterid=#{shelterid},status=#{status},adopterid=#{adopterid} " +
            "where animalid=#{animalid}")
    void updateAnimal(Animals animals);

    @Delete("delete from animals where animalid=#{animalid}")
    void deleteAnimal(Integer animalid);

    //这里不用注解写sql，因为是动态sql要使用配置文件的方式，不然很麻烦
    List<Animals> pageList(Integer shelterid, Integer speciesid, String gender,String name, String age, String status);


    @Update("update animals set status=#{status},adopterid=#{userid} where animalid=#{animalid}")
    void updateAdoptStatus(String status,Integer animalid, Integer userid);
}
