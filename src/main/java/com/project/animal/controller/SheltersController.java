package com.project.animal.controller;



import com.project.animal.entity.Animals;
import com.project.animal.entity.Result;
import com.project.animal.entity.Shelters;
import com.project.animal.service.IAnimalsService;
import com.project.animal.service.IUsersService;
import com.project.animal.service.SheltersService;
import com.project.animal.utils.ThreadLocalUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * (Shelters)表控制层
 *
 * @author makejava
 * @since 2024-03-15 11:07:05
 */
@RestController
@RequestMapping("/shelters")
public class SheltersController{
    /**
     * 服务对象
     */
    @Autowired
    private SheltersService sheltersService;

    @Autowired
    private IUsersService usersService;

    @Autowired
    private IAnimalsService animalService;

    //首先要判断是不是管理员
    public boolean isAdmin(){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userid = (Integer) map.get("id");
        //根据userid查询用户是否是管理员
        boolean isadmin = usersService.isAdmin(userid);
        //如果isadmin==0，说明不是管理员，不可以修改
        return isadmin;
    }

    //查询全部收养所
    @GetMapping("/getAllSh")
    public Result<List<Shelters>> getAllSh(){
         List<Shelters>  shelters = sheltersService.getAllSh();
         return Result.success(shelters);
    }


    //通过shelterid查询收养所
    //@GetMapping("/{userId}")指定了URL路径为/users/{userId}
    //而@PathVariable Long userId则将URL中的{userId}部分映射到方法参数userId上
    //从而实现了根据用户ID获取用户信息的功能。
    //总的来说，PathVariable注解的作用是帮助我们在Spring MVC中从URL中获取参数值，
    //实现灵活的请求处理和RESTful风格的编程
    @GetMapping("/getShById/{shelterid}")
    public Result<Shelters> getShById(@PathVariable Integer shelterid){
        Shelters shelters =  sheltersService.getShById(shelterid);
        return Result.success(shelters);
    }


    //添加收养所
    @PostMapping("/addSh")
    public Result addSh(@RequestBody Shelters sh){
        if(!isAdmin()){
            return Result.error("对不起，您没有权限添加收养所信息");
        }
        //不能添加重复的
        if(sheltersService.getShByName(sh.getSheltername())!=null){
            return Result.error("对不起，该收养所已存在");
        }

        sheltersService.addSh(sh);
        return Result.success();
    }

    //删除收养所
    @PostMapping("/deleteSh/{shelterid}")
    public Result deleteSh(@PathVariable Integer shelterid){
        if(!isAdmin()){
            return Result.error("对不起，您没有权限删除收养所信息");
        }
        Shelters shelters = sheltersService.getShById(shelterid);
        //通过收养所id查动物表
        List<Animals> animals = animalService.listByShelterId(shelterid);
        System.out.println(animals);
        //如果animals正在使用不能删除
        if(animals.size()>0){
            return Result.error("对不起，该收养所正在使用，不能删除");
        }

        //如果不存在
        if(shelters == null ){
            return Result.error("对不起，该收养所不存在");
        }

        sheltersService.deleteSh(shelterid);
        return Result.success();
    }

    //修改收养所
    @PostMapping("/updateSh")
    public Result updateSh(@RequestBody Shelters sh){
        if(!isAdmin()){
            return Result.error("对不起，您没有权限修改收养所信息");
        }
        if(sheltersService.getShById(sh.getShelterid()) == null ){
            return Result.error("对不起，该收养所不存在");
        }
        //不是本身
        Shelters shelters = sheltersService.getShByName(sh.getSheltername());
        if(shelters!=null&&shelters.getShelterid()!=sh.getShelterid()){
            return Result.error("对不起，该收养所已存在");
        }
        sheltersService.updateSh(sh);
        return Result.success();
    }



}

