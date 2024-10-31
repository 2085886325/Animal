package com.project.animal.service;

import com.project.animal.entity.Animals;
import com.project.animal.entity.PageBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rhy
 * @since 2024-03-04
 */
public interface IAnimalsService  {

    //获取动物列表
    List<Animals> list();

    List<Animals> listByAdopterId();

    List<Animals> listBySpeciesId(Integer speciesid);

    List<Animals> listByShelterId(Integer shelterid);


    /**
     * 根据动物ID查找对应的动物信息。
     *
     * @param animalid 动物的唯一标识符，类型为Integer。
     * @return Animals 返回找到的动物信息对象，如果找不到则返回null。
     */
    Animals findAnimalById(Integer animalid);

    void addAnimal(Animals animals);

    void updateAnimal(Animals animals);

    void deleteAnimal(Integer animalid);

    PageBean<Animals> pageList(Integer page, Integer size , Integer shelterid, Integer speciesid , String gender,String name, String age, String status);

    void updateAdoptStatus(String status,Integer animalid, Integer userid);
}
