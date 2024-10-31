package com.project.animal.controller;



import com.project.animal.entity.*;
import com.project.animal.service.AdoptapplyService;
import com.project.animal.service.AdoptionrecordsService;
import com.project.animal.service.IAnimalsService;
import com.project.animal.service.IUsersService;
import com.project.animal.utils.ThreadLocalUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * (Adoptionrecords)表控制层
 *
 * @author makejava
 * @since 2024-03-09 12:04:20
 */
@RestController
@RequestMapping("/records")
@CrossOrigin//允许跨域
public class AdoptionrecordsController{
    //判断是否是管理员需要
    @Autowired
    private IUsersService usersService;
    /**
     * 服务对象
     */
    @Autowired
    private AdoptionrecordsService adoptionrecordsService;


    @Autowired
    private AdoptapplyService adoptapplyService;

    @Autowired
    private IAnimalsService animalService;

    /**
     * 通过动物id查询领养记录
     *
     * @param animalid 动物id
     * @return 实例对象
     */
    @GetMapping("/queryByAnimalId")
    public Result queryByAnimalId(Integer animalid){
        Adoptionrecords adoptionrecords=adoptionrecordsService.queryByAnimalId(animalid);
        return Result.success(adoptionrecords);
    }

    //通过用户id查询收养记录
    @GetMapping("/queryByUserId")
    public Result queryByUserId(Integer userid){
        List<Adoptionrecords> list = adoptionrecordsService.queryByUserId(userid);
        return Result.success(list);
    }

    /**
     * 查询所有数据
     *
     * @return 对象列表
     */
    @GetMapping("/list")
    public Result<List<RecordDTO>> queryAll(){
        List<RecordDTO> list = adoptionrecordsService.queryAll();
        return Result.success(list);
    }


    //管理员相关权限模块
    //首先要判断是不是管理员
    public boolean isAdmin(){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userid = (Integer) map.get("id");
        //根据userid查询用户是否是管理员
        boolean isadmin = usersService.isAdmin(userid);
        //如果isadmin==0，说明不是管理员，不可以修改
        return isadmin;
    }

    //新增申请领养记录（用户申请得到管理员同意后才会新增记录,所以只有管理员有权限调用此方法）
    @PostMapping("/addRecords")
    public Result addAdoptionrecords(@RequestBody Adoptionrecords adoptionrecords){
        if (!isAdmin()){
            return Result.error("非管理员，无权操作");
        }
        //判断该动物是否已被领养
        Adoptionrecords hasRecords=adoptionrecordsService.queryByAnimalId(adoptionrecords.getAnimalid());
        if(hasRecords!=null){
            return Result.error("该动物已被领养，请勿重复领养");
        }
        //首先要判断有没有这个用户和这个动物
        Animals animalById = animalService.findAnimalById(adoptionrecords.getAnimalid());
        if(animalById==null){
            return Result.error("该动物不存在");
        }
        //判断该用户是否存在
        if(usersService.findByUserId(adoptionrecords.getUserid())==null){
            return Result.error("该用户不存在");
        }
        //还得判断动物有没有正在申请的，如果正在申请那也不能添加
        Adoptapply adoptapply=adoptapplyService.selectByAnimalId(adoptionrecords.getAnimalid());
        if(adoptapply!=null){//说明有待审核的动物申请
            //修改该记录为通过，并把传入的animalid和userid修改
            adoptapplyService.updateAdoptStatus(adoptapply.getApplicationid(),"1",adoptionrecords.getAnimalid(),adoptionrecords.getUserid());
        }else{//没有申请记录
            //因为这是申请成功的记录 所以申请成功的时候也要向申请表里面添加一条记录
            adoptapplyService.addApply(new Adoptapply(null,adoptionrecords.getAnimalid(),adoptionrecords.getUserid(),new Date(),"1","系统添加","系统添加","系统添加","系统添加",null));
        }

        //更新动物表中的status为1已领养
        animalService.updateAdoptStatus("1",adoptionrecords.getAnimalid(),adoptionrecords.getUserid());

        adoptionrecordsService.addRecords(adoptionrecords);
        return Result.success("添加收养记录成功");
    }

    //删除收养记录
    @PostMapping("/delRecords")
    public Result delRecords(@RequestParam Integer recordId){
        if (!isAdmin()){
            return Result.error("非管理员，无权操作");
        }
        //通过recordId查询领养记录
        Adoptionrecords adoptionrecords = adoptionrecordsService.selectByRecordId(recordId);
        //删除记录就需要修改对应的动物表和领养申请表
        //领养申请表设置为0待审核，动物表设置为2待审核
        //查询adoptapply表
        Adoptapply adoptapply=adoptapplyService.selectByAnimalId(adoptionrecords.getAnimalid());
        if (adoptapply!=null){
            adoptapplyService.updateApply(new Adoptapply(adoptapply.getApplicationid(),adoptionrecords.getAnimalid(),adoptionrecords.getUserid(),adoptapply.getApplicationdate(),"0",adoptapply.getAdoptionmotivation(),adoptapply.getFamilyenvironment(),adoptapply.getPersonalexperience(),adoptapply.getAdditionalinformation(),adoptapply.getRejectionreason()));
        }
        animalService.updateAdoptStatus("2",adoptionrecords.getAnimalid(),null);
        adoptionrecordsService.delRecords(recordId);
        return Result.success("删除收养记录成功");
    }

    //修改收养记录
    @PostMapping("/updateRecords")
    public Result updateAdoptionrecords(@RequestBody Adoptionrecords adoptionrecords){
        if (!isAdmin()){
            return Result.error("非管理员，无权操作");
        }
        //通过记录id查询这条数据
        Adoptionrecords hasRecords=adoptionrecordsService.selectByRecordId(adoptionrecords.getAdoptionrecordid());
        //通过animalid查询这条数据
        Adoptionrecords hasTrueRecords=adoptionrecordsService.queryByAnimalId(adoptionrecords.getAnimalid());
        if(hasTrueRecords!=null&&hasTrueRecords.getAdoptionrecordid() != adoptionrecords.getAdoptionrecordid()){
            return Result.error("该动物已成功被领养，请勿重复领养");
        }
        //通过传入的AnimalId查询领养申请表
        Adoptapply adoptapply=adoptapplyService.selectByAnimalId(adoptionrecords.getAnimalid());
        //通过未更改之前的AnimalId查询领养申请表
        Adoptapply preAdoptApply=adoptapplyService.selectByAnimalId(hasRecords.getAnimalid());
        //没有被拒绝并且还在这记录里面
        //此处暂时设置为可以修改
//        if(adoptapply!=null&&!adoptapply.getStatus().equals("2")){
//            return Result.error("该动物正在申请领养或已被领养，请勿重复领养");
//        }
        //首先要判断有没有传入的这个用户和这个动物
        Animals animalById = animalService.findAnimalById(adoptionrecords.getAnimalid());
        if(animalById==null){
            return Result.error("该动物不存在");
        }
        //判断该用户是否存在
        if(usersService.findByUserId(adoptionrecords.getUserid())==null){
            return Result.error("该用户不存在");
        }
        //因为这是修改为申请成功的记录 所以申请成功的时候也要向申请表里面修改记录

        //如果该动物未领养，说明没有申请，则新增一条通过的申请
        String status = animalService.findAnimalById(adoptionrecords.getAnimalid()).getStatus();
        if (status.equals("0")){//未领养
            //这里要新增一条通过申请，因为这是申请成功的记录表
            adoptapplyService.addApply(new Adoptapply(null,adoptionrecords.getAnimalid(),adoptionrecords.getUserid(),new Date(),"1","系统添加","系统添加","系统添加","系统添加",null));
        }else if(status.equals("1")){//已领养
            if (preAdoptApply.getUserid()!=adoptionrecords.getUserid()&&preAdoptApply.getAnimalid()==adoptionrecords.getAnimalid()) {//说明修改用户了
                //申请表中也要改一下用户
                adoptapplyService.updateAdoptStatus(preAdoptApply.getApplicationid(),"1",adoptionrecords.getAnimalid(),adoptionrecords.getUserid());
            }
        }else if (status.equals("2")) {//待审核
            //把新传入的动物id和userid查询到的adoptapply进行更改为通过
            adoptapplyService.updateAdoptStatus(adoptapply.getApplicationid(),"1",adoptionrecords.getAnimalid(),adoptionrecords.getUserid());
            //把原来申请成功记录待审核改为0，其余内容不变
            adoptapplyService.updateAdoptStatus(preAdoptApply.getApplicationid(),"0",preAdoptApply.getAnimalid(),preAdoptApply.getUserid());
        }
        //更新动物表中新的动物的status为1已领养并更新原表中原动物为2待审核
        animalService.updateAdoptStatus("2",hasRecords.getAnimalid(),null);
//        System.out.println("成功animalService1");
        animalService.updateAdoptStatus("1",adoptionrecords.getAnimalid(),adoptionrecords.getUserid());
//        System.out.println("成功animalService2");


        adoptionrecordsService.updateRecords(adoptionrecords);
        return Result.success("修改收养记录成功");
    }



}

