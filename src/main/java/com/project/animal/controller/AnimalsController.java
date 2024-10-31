package com.project.animal.controller;


import com.project.animal.anno.AnimalsStatus;
import com.project.animal.entity.*;
import com.project.animal.service.*;
import com.project.animal.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
//import org.apache.tomcat.util.net.AbstractEndpoint;
/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author why
 * @since 2024-03-04
 */
@RestController
@Validated//为了在动物的状态上面使用验证注解
@RequestMapping("/animal")
public class AnimalsController {

    @Autowired
    private IAnimalsService animalsService;

    @Autowired
    private IUsersService usersService;


    @Autowired
    private ISpeciesService speciesService;

    @Autowired
    private SheltersService sheltersService;

    @Autowired
    private AdoptapplyService adoptapplyService;

    @Autowired
    private AdoptionrecordsService adoptionrecordsService;

    //查询所有动物
    @GetMapping("/list")
    public Result<List<Animals>> list(/*@RequestHeader(name = "Authorization") String token, HttpServletResponse response*/) {
       /* try {
            Map<String, Object> claims = JwtUtil.parseToken(token);
            //放行
            return Result.success("这里是所有动物的数据");
        } catch (Exception e) {
            //解析失败，不放行，登录失败
            response.setStatus(401);
            return Result.error("失败");
        }*/
        List<Animals> animals = animalsService.list();
        //有拦截器进行拦截就不需要这里再拦截，设置拦截器是为了不用每次访问都去设定判断是否携带正确的token
        return Result.success(animals);
    }

    //通过收养人id查询该用户收养的流浪动物
    @GetMapping("/listByAdopterId")
    public Result<List<Animals>> listByAdopterId() {
        List<Animals> animals = animalsService.listByAdopterId();
        return Result.success(animals);
    }

    //通过物种id查询归属于这一物种的动物列表
    @GetMapping("/listBySpeciesId")
    public Result<List<Animals>> listBySpeciesId(@RequestParam Integer speciesid) {
        List<Animals> animals = animalsService.listBySpeciesId(speciesid);
        return Result.success(animals);
    }

    //通过收容所id查询对应的动物列表
    @GetMapping("/listByShelterId")
    public Result<List<Animals>> listByShelterId(@RequestParam Integer shelterid) {
        List<Animals> animals = animalsService.listByShelterId(shelterid);
        return Result.success(animals);
    }


    //分页查询加动态sql
    //前端请求的路径是/pageList?page=1&size=10&shelterid=1&speciesid=1&gender=1&age=1&status=1

    @GetMapping("/pageList")
    public Result<PageBean<Animals>> pageList(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required = false) Integer shelterid, @RequestParam(required = false) Integer speciesid, @RequestParam(required = false) String shelterName, @RequestParam(required = false) String speciesName, @RequestParam(required = false) String name, @RequestParam(required = false) String gender, @RequestParam(required = false) String age, @RequestParam(required = false) @AnimalsStatus String status) {
        if (speciesName != null && speciesName != "") {
            speciesid = speciesService.listBySpeciesName(speciesName);
//            System.out.println(speciesid);
        }
        if (shelterName != null && shelterName != "") {
            Shelters shelter = sheltersService.getShByName(shelterName);
            shelterid = shelter.getShelterid();
        }
        //如果传入的字段为""则设置为null
        //此处已经在xml中限制了，所以这里就不用判断了


        if (gender == "男") {
            gender = "1";
        } else if (gender == "女") {
            gender = "0";
        } else {

        }
        //分页查询
        PageBean<Animals> animals = animalsService.pageList(page, size, shelterid, speciesid, gender, name, age, status);
        return Result.success(animals);
    }


    //管理员相关权限模块

    //首先要判断是不是管理员
    public boolean isAdmin() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userid = (Integer) map.get("id");
        //根据userid查询用户是否是管理员
        boolean isadmin = usersService.isAdmin(userid);
        //如果isadmin==0，说明不是管理员，不可以修改
        return isadmin;
    }

    //新增流浪动物记录
    @PostMapping("/addAnimal")
    public Result addAnimal(@RequestBody @Validated(Animals.Add.class) Animals animals) {
//        Map<String,Object> map = ThreadLocalUtil.get();
//        Integer userid = (Integer) map.get("id");
//        //根据userid查询用户是否是管理员
//        boolean isadmin = usersService.isAdmin(userid);
        //如果isadmin==1，说明是管理员，可以添加
        if (!isAdmin()) {
            return Result.error("非管理员无权限修改");
        }

        if (animals.getStatus().equals("1")) {
            //说明是已经领养状态
            //申请表和记录表分别添加一条数据
            adoptapplyService.addApply(new Adoptapply(null, animals.getAnimalid(), animals.getAdopterid(), null, "1", "系统添加", "系统添加", "系统添加", "系统添加", ""));
            //记录表无论如何也得添加 因为修改为了已经领养
            adoptionrecordsService.addRecords(new Adoptionrecords(null, animals.getAnimalid(), animals.getAdopterid(), new Date(), new Date()));
        }
        //未领养直接添加就行

        //拒绝添加待审核的动物
        if (animals.getStatus().equals("2")) {
            return Result.error("不能添加待审核的动物");
        }

        animalsService.addAnimal(animals);
        return Result.success();
    }

    //通过动物id修改动物信息
    @PostMapping("/updateAnimal")
    public Result updateAnimal(@RequestBody @Validated(Animals.Update.class) Animals animals) {
        if (!isAdmin()) {
            return Result.error("非管理员无权限修改");
        }
        //首先通过传来的动物id获取当前动物
        Animals preAnimal = animalsService.findAnimalById(animals.getAnimalid());
        //判断动物的状态有没有变化
        if (!preAnimal.getStatus().equals(animals.getStatus())) {
            //修改了状态待审核变成了未领养，则需要修改对应的领养申请表
            if (animals.getStatus().equals("0")) {
                //说明从待审核或者通过改为了未领养,则需要删除领养申请表中的数据
                //首先通过动物id获取到这条数据
                Adoptapply adoptapply = adoptapplyService.selectByAnimalId(animals.getAnimalid());
                if (adoptapply != null) {
                    //删除领养申请表中的数据
                    adoptapplyService.deleteApply(adoptapply.getApplicationid());
                }
                //如果是从通过或者是待审核改过来的还需要删除对应的成功记录表
                if (preAnimal.getStatus().equals("1")) {
                    //获取recordid
                    Adoptionrecords adoptionrecords = adoptionrecordsService.queryByAnimalId(animals.getAnimalid());
                    if (adoptionrecords != null) {
                        //删除成功记录
                        adoptionrecordsService.delRecords(adoptionrecords.getAdoptionrecordid());
                    }
                }
            } else if (animals.getStatus().equals("1")) {
                //说明前端改为了已领养，则需要修改领养申请表中的数据为已领养
                //如果没有这条记录则需要在领养表中添加一条数据
                //另外还需要在记录表中添加一条数据
                //先通过动物id获取领养申请表
                Adoptapply adoptapply = adoptapplyService.selectByAnimalId(animals.getAnimalid());
                if (adoptapply != null) {
                    adoptapplyService.updateAdoptStatus(adoptapply.getApplicationid(), "1", animals.getAnimalid(), animals.getAdopterid());
                } else {//没有这条记录则需要添加一条记录，因为修改为了已经领养
                    adoptapplyService.addApply(new Adoptapply(null, animals.getAnimalid(), animals.getAdopterid(), null, "1", "系统添加", "系统添加", "系统添加", "系统添加", ""));
                }
                //记录表无论如何也得添加 因为修改为了已经领养
                adoptionrecordsService.addRecords(new Adoptionrecords(null, animals.getAnimalid(), animals.getAdopterid(), new Date(), new Date()));
            } else if (animals.getStatus().equals("2")) {
                return Result.error("禁止修改为待审核");
            } else {
                return Result.error("状态错误");
            }
        }else{//动物的状态没变化，看看领养人有没有变化
            //动物有领养人的情况下
            if (preAnimal.getAdopterid()!=null&&!(preAnimal.getAdopterid().equals(animals.getAdopterid())) ) {
                //修改了领养人
                //如果修改了领养人，则需要修改领养申请表中的数据
                Adoptapply adoptapply = adoptapplyService.selectByAnimalId(animals.getAnimalid());
                if (adoptapply != null) {
                    adoptapplyService.updateAdoptStatus(adoptapply.getApplicationid(), "1", animals.getAnimalid(), animals.getAdopterid());
                }
                //也要修改领养记录中的数据，先通过动物id获取记录
                Adoptionrecords adoptionrecords = adoptionrecordsService.queryByAnimalId(animals.getAnimalid());
                if (adoptionrecords != null){
                    adoptionrecordsService.updateRecords(new Adoptionrecords(adoptionrecords.getAdoptionrecordid(), animals.getAnimalid(), animals.getAdopterid(), adoptionrecords.getApplicationdate(), new Date()));
                }
            }

        }

        animalsService.updateAnimal(animals);
        return Result.success(animals);
    }

    //通过传过来的动物id删除流浪动物记录
    @PostMapping("/deleteAnimal")
    public Result deleteAnimal(@RequestParam Integer animalid) {
        Animals animal = animalsService.findAnimalById(animalid);
        if (!isAdmin()) {
            return Result.error("非管理员无权限删除");
        }
        if (animal.getStatus().equals("1")) {
            //因为已经领养，所以要删除成功的记录
            Adoptionrecords adoptionrecords = adoptionrecordsService.queryByAnimalId(animalid);
            if (adoptionrecords != null) {
                adoptionrecordsService.delRecords(adoptionrecords.getAdoptionrecordid());
            }

        }
        //如果是已领养或者待审核状态，则需要删除领养申请表中的数据后再删除此动物
        Adoptapply adoptapply = adoptapplyService.selectByAnimalId(animalid);
        if (adoptapply != null) {
            adoptapplyService.deleteApply(adoptapply.getApplicationid());
        }


        animalsService.deleteAnimal(animalid);
        return Result.success("已删除（" + animal.getName() + "）的记录");
    }


    //通过动物id查询动物
    @GetMapping("/findAnimalById")
    public Result findAnimalById(@RequestParam Integer animalid) {
        Animals animals = animalsService.findAnimalById(animalid);
        return Result.success(animals);
    }

    //未领养的个数 待审核的个数 已领养的个数，制作echarts用
    @GetMapping("/getAnimalChart")
    public Result getAnimalChart() {
        List<Animals> list = animalsService.list();
        Map<String, Long> collect = list.stream().collect(Collectors.groupingBy(Animals::getStatus, Collectors.counting()));
        //返回给前端的list
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(collect)) {
            for (String key : collect.keySet()) {
                Map<String, Object> map = new HashMap<>();
                map.put("value", collect.get(key));

                if (key.equals("0")) {
                    key = "未领养";
                } else if (key.equals("1")) {
                    key = "已领养";
                } else if (key.equals("2")) {
                    key = "待审核";
                } else {

                }
                map.put("name", key);
                mapList.add(map);
            }
        }
        return Result.success(mapList);
    }


    //获取动物的分类名，和每个分类下的动物数量，制作echarts用
    @GetMapping("/getAnimalTypeChart")
    public Result getAnimalTypeChart() {
        List<Animals> list = animalsService.list();
        Map<Integer, Long> collect = list.stream().collect(Collectors.groupingBy(Animals::getSpeciesid, Collectors.counting()));
        //返回给前端的list9
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(collect)) {
            for (Integer key : collect.keySet()) {
                Map<String, Object> map = new HashMap<>();
                map.put("value", collect.get(key));
                Species species = speciesService.findSpeciesById(key);
                map.put("name", species.getSpeciesName());
                mapList.add(map);
            }
        }
        return Result.success(mapList);
    }

    //获取用户注册时间和这个月注册的人数，返回给前端作为图表数据
    @GetMapping("/getRegisterChart")
    public Result getRegisterChart() {
        List<Users> list = usersService.getUserList();


// 按月份分组并统计注册人数，并按照月份大小排序
        Map<Month, Long> monthlyRegistrations = list.stream()
                .collect(Collectors.groupingBy(
                        user -> user.getRegistrationDate().getMonth(), // 使用月份的枚举值作为分组依据
                        TreeMap::new, // 使用 TreeMap 按键的自然顺序排序
                        Collectors.counting()
                ));
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(monthlyRegistrations)) {
            for (Month key : monthlyRegistrations.keySet()) {
                Map<String, Object> map = new HashMap<>();
                map.put("value", monthlyRegistrations.get(key));
                map.put("name", key);
                mapList.add(map);
            }
        }
        return Result.success(mapList);
    }


}
