package com.project.animal.controller;


import com.project.animal.anno.AnimalsStatus;
import com.project.animal.entity.Animals;
import com.project.animal.entity.Result;
import com.project.animal.entity.Species;
import com.project.animal.service.IAnimalsService;
import com.project.animal.service.ISpeciesService;
import com.project.animal.service.IUsersService;
import com.project.animal.utils.ThreadLocalUtil;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * SpeciesController 类负责处理与动物物种相关的HTTP请求。
 */
@RestController
@Validated //参数校验需要
@RequestMapping("/species")
public class SpeciesController {

    @Autowired
    private ISpeciesService speciesService;

    @Autowired
    private IUsersService usersService;

    @Autowired
    private IAnimalsService animalsService;

    //首先要判断是不是管理员
    public boolean isAdmin(){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userid = (Integer) map.get("id");
        //根据userid查询用户是否是管理员
        boolean isadmin = usersService.isAdmin(userid);
        //如果isadmin==0，说明不是管理员，不可以修改
        return isadmin;
    }

    //新增物种类别
    @PostMapping("/addSpecies")
    public Result addSpecies(@Pattern(regexp = "^\\S{1,8}$") String speciesName)
    {
        if (!isAdmin()){
            return Result.error("对不起，您没有权限添加物种类别");
        }
        if (speciesService.listBySpeciesName(speciesName)!=null){
            return Result.error("该物种类别已存在");
        }
        if (speciesName==null||speciesName.equals("")){
            return Result.error("物种类别不能为空");
        }
        speciesService.addSpecies(speciesName);
        return Result.success();
    }

    //删除物种类别
    @PostMapping("/deleteSpecies")
    public Result deleteSpecies(@RequestParam Integer speciesId)
    {
        if (!isAdmin()){
            return Result.error("对不起，您没有权限删除物种类别");
        }
        if (animalsService.listBySpeciesId(speciesId).size()!=0){
            return Result.error("该物种类别下有动物，无法删除");
        }
        speciesService.deleteSpecies(speciesId);
        return Result.success();
    }

    //通过物种id修改物种名
    @PostMapping("/updateSpecies")// @Validated作用于对象上
    public Result updateSpecies(@RequestBody @Validated Species species)
    {
        if (!isAdmin()){
            return Result.error("对不起，您没有权限修改物种类别");
        }
        //通过speciesid查询
        Integer speciesid= speciesService.listBySpeciesName(species.getSpeciesName());
        Species preSpecies = speciesService.findSpeciesById(species.getSpeciesId());
        //如果speciesid不为空，且speciesid不等于preSpecies.getSpeciesId()，说明该物种类别已存在
        if (speciesid!=null&&speciesid!=preSpecies.getSpeciesId()){
            return Result.error("该物种类别已存在");
        }
        speciesService.updateSpecies(species);
        return Result.success();
    }

    //通过物种id查询对应的物种类别
    @GetMapping("/findSpeciesById")
    public Result<Species> findSpeciesById(@RequestParam Integer speciesId)
    {
        Species species = speciesService.findSpeciesById(speciesId);
        return Result.success(species);
    }

    //查询所有物种类别
    @GetMapping("/list")
    public Result<List<Species>> list()
    {
        List<Species> speciesList = speciesService.list();
        return Result.success(speciesList);
    }

}
