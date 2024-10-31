package com.project.animal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.project.animal.entity.Animals;
import com.project.animal.entity.PageBean;
import com.project.animal.entity.Result;
import com.project.animal.mapper.AnimalsMapper;
import com.project.animal.service.IAnimalsService;
import com.project.animal.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rhy
 * @since 2024-03-04
 */
@Service
public class AnimalsServiceImpl implements IAnimalsService {

    @Autowired
    private AnimalsMapper animalsMapper;

    @Override
    public List<Animals> list() {
        return animalsMapper.list();
    }

    @Override
    public List<Animals> listByAdopterId() {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer adopterid = (Integer) map.get("id");
        //当前登录的用户id
        //System.out.println(adopterid);
        return animalsMapper.listByAdopterId(adopterid);
    }

    @Override
    public List<Animals> listBySpeciesId(Integer speciesid) {
        return animalsMapper.listBySpeciesId(speciesid);
    }

    @Override
    public List<Animals> listByShelterId(Integer shelterid) {
        return animalsMapper.listByShelterId(shelterid);
    }

    @Override
    public Animals findAnimalById(Integer animalid) {
        return animalsMapper.findAnimalById(animalid);
    }

    @Override
    public void addAnimal(Animals animals) {
        animalsMapper.addAnimal(animals);
    }

    @Override
    public void updateAnimal(Animals animals) {
        animalsMapper.updateAnimal(animals);
    }

    @Override
    public void deleteAnimal(Integer animalid) {
        animalsMapper.deleteAnimal(animalid);
    }

    @Override
    public PageBean<Animals> pageList(Integer page, Integer size, Integer shelterid, Integer speciesid , String gender,String name, String age, String status) {
        //创建bean对象
        PageBean<Animals> pageBean = new PageBean<>();

        //开启分页，必须指定两个参数，必须在sql语句执行前
        // pageNum：当前页
        // pageSize：当前页的数据条数
        //不需要返回值
        PageHelper.startPage(page, size);
        //调用mapper层的分页查询方法
        List<Animals> list = animalsMapper.pageList(shelterid, speciesid, gender, name, age, status);
        //此处为什么强转是因为分页查询方法返回的是Page对象，而PageBean需要的是List对象
        Page<Animals> pg = (Page<Animals>) list;
        pageBean.setTotal(pg.getTotal());//此处是long类型
        pageBean.setItems(pg.getResult());
        return pageBean;
    }

    @Override
    public void updateAdoptStatus(String status, Integer animalid, Integer userid) {
        animalsMapper.updateAdoptStatus(status,animalid,userid);
    }
}
