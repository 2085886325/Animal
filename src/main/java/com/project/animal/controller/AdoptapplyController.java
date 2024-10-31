package com.project.animal.controller;



import com.project.animal.entity.*;
import com.project.animal.service.AdoptapplyService;
import com.project.animal.service.AdoptionrecordsService;
import com.project.animal.service.IAnimalsService;
import com.project.animal.service.IUsersService;
import com.project.animal.utils.ThreadLocalUtil;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * (Adoptapply)表控制层
 *
 * @author makejava
 * @since 2024-03-13 15:51:27
 */
@RestController
@RequestMapping("/adoptapply")
@Validated
@CrossOrigin//允许跨域
public class AdoptapplyController{

    @Autowired
    private IUsersService usersService;
    /**
     * 服务对象
     */
    @Autowired
    private AdoptapplyService adoptapplyService;

    @Autowired
    private AdoptionrecordsService adoptionrecordsService;


    @Autowired
    private IAnimalsService animalService;


    //用户点击申请后新增一条申请
    //但这个申请必须是未申请的动物，否则失效
    //用户申请的时候不能提交拒绝原因和申请状态字段
    @PostMapping("/addApply")
    public Result addApply(@RequestBody @Validated(Adoptapply.Add.class) Adoptapply adoptapply){
        //将拒绝原因和申请状态置空
        adoptapply.setStatus("0");
        adoptapply.setRejectionreason(null);
        //通过查是否有记录并且不是拒绝 判断该动物是否已经被申请领养
        Adoptapply selectAdoptapply = adoptapplyService.selectByAnimalId(adoptapply.getAnimalid());
        //&&!selectAdoptapply.getStatus().equals("2")
        //已经在sql中判断状态不是2拒绝
        if(null != selectAdoptapply){
            return Result.error("该动物已经被申请领养，请勿重复申请");
        }
        //查询用户是否已经填写过手机号邮箱和地址信息，未填写将不允许用户进行领养申请
        Users user = usersService.findByUserId(adoptapply.getUserid());
        if(user!=null){
            System.out.println(user);
            if(user.getEmail()==null||user.getPhone()==null||user.getAddress()==null){
                return Result.error("请先填写个人的邮箱，手机号和地址再进行申请！");
            }
            if(user.getEmail().equals("") || user.getPhone().equals("") || user.getAddress().equals("")){
                return Result.error("请先填写个人的邮箱，手机号和地址再进行申请！");
            }
        }else{
            return Result.error("申请者不存在");
        }
        //更新动物表中的status为2待审核
        animalService.updateAdoptStatus("2",adoptapply.getAnimalid(),null);
        adoptapplyService.addApply(adoptapply);
        return Result.success();
    }
    //通过申请id查询申请表的单个记录
    @GetMapping("/selectByApplyId")
    public Result<Adoptapply> selectByApplyId(@RequestHeader Integer applicationid){
        if (!isAdmin()){
            return Result.error("无权限进行操作");
        }
        Adoptapply adoptapply = adoptapplyService.selectByApplyId(applicationid);
        if (adoptapply == null){
            return Result.error("未查询到该记录");
        }
        return Result.success(adoptapply);
    }


    //通过用户id查询申请表
    @GetMapping("/selectByUserId")
    public Result<List<AdoptapplyDTO>> selectByUserId(@RequestHeader Integer userid){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer user = (Integer) map.get("id");
        if (!isAdmin()&&user!=userid){
            return Result.error("无权限进行操作");
        }
        List<AdoptapplyDTO> adoptapply = adoptapplyService.selectByUserId(userid);
        if (adoptapply.size()==0){
            return Result.error("未查询到该用户记录");
        }

        return Result.success(adoptapply);
    }




    //查询收养申请表
    @GetMapping("/selectAllApply")
    public Result<List<AdoptapplyDTO>> selectApply(){
        List<AdoptapplyDTO> adoptapply = adoptapplyService.selectAll();
        return Result.success(adoptapply);
    }

    //管理员相关权限
    //首先判断是不是管理员
    public boolean isAdmin(){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userid = (Integer) map.get("id");
        //根据userid查询用户是否是管理员
        boolean isadmin = usersService.isAdmin(userid);
        //如果isadmin==0，说明不是管理员，不可以修改
        return isadmin;
    }

    //通过applicationid删除收养申请记录
    @PostMapping("/deleteApply")
    public Result deleteApply(@RequestHeader @NotNull Integer applicationid){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer user = (Integer) map.get("id");
        //通过applicationid查到这条记录
        Adoptapply adoptapply = adoptapplyService.selectByApplyId(applicationid);
        //通过animalid查动物
        //Animals animals = animalService.findAnimalById(adoptapply.getAnimalid());
       if (adoptapply!=null){
           if (!isAdmin()&&user!=adoptapply.getUserid()){
               return Result.error("无权限进行删除操作");
           }
           //修改动物的状态为0
           animalService.updateAdoptStatus("0",adoptapply.getAnimalid(),null);
           //通过animalid查记录表
           Adoptionrecords adoptrecords = adoptionrecordsService.queryByAnimalId(adoptapply.getAnimalid());
           //同时需要删除对应的成功记录，并修改动物的状态
           if(adoptrecords!=null){
               adoptionrecordsService.delRecords(adoptrecords.getAdoptionrecordid());
           }
       }
        
        adoptapplyService.deleteApply(applicationid);
        return Result.success();
    }

    //修改收养申请记录
    @PostMapping("/updateApply")
    public Result updateApply(@RequestBody @Validated(Adoptapply.Update.class) Adoptapply adoptapply){
        if (!isAdmin()){
            return Result.error("无权限进行修改操作");
        }
        //判断是否通过
        if(adoptapply.getStatus().equals("1")){//假如通过
            agreeApply(adoptapply.getApplicationid());
            adoptapply.setRejectionreason(null);
        }else if(adoptapply.getStatus().equals("2")){//假如拒绝
            rejectApply(adoptapply.getApplicationid(),adoptapply.getRejectionreason());
        }else{//假如待审核
            adoptapply.setRejectionreason(null);
            //更新动物表中的status为2待审核
            animalService.updateAdoptStatus("2",adoptapply.getAnimalid(),null);
            //查询记录表中是否有数据，如果有则删除数据
            Adoptionrecords adoptrecords = adoptionrecordsService.queryByAnimalId(adoptapply.getAnimalid());

            if(adoptrecords!=null){
                adoptionrecordsService.delRecords(adoptrecords.getAdoptionrecordid());
            }

        }
        adoptapplyService.updateApply(adoptapply);
        return Result.success();
    }

    //管理员的新增操作
    @PostMapping("/addApplyByAdmin")
    public Result addApplyByAdmin(@RequestBody @Validated(Adoptapply.Add.class) Adoptapply adoptapply){
        if (!isAdmin()){
            return Result.error("无权限进行添加操作");
        }
        //通过查是否有记录并且不是拒绝 判断该动物是否已经被申请领养
        Adoptapply selectAdoptapply = adoptapplyService.selectByAnimalId(adoptapply.getAnimalid());
        //&&!selectAdoptapply.getStatus().equals("2")
        //已经在sql中判断
        if(null !=selectAdoptapply ){
            return Result.error("该动物已经被申请领养，请勿重复申请");
        }
        //判断是否通过
        if(adoptapply.getStatus().equals("1")){//假如通过
            agreeApply(adoptapply.getApplicationid());
            adoptapply.setRejectionreason(null);
        }else if(adoptapply.getStatus().equals("2")){//假如拒绝
            rejectApply(adoptapply.getApplicationid(),adoptapply.getRejectionreason());
        }else{//假如待审核
            adoptapply.setRejectionreason(null);
            //更新动物表中的status为2待审核
            animalService.updateAdoptStatus("2",adoptapply.getAnimalid(),null);
        }

        adoptapplyService.addApply(adoptapply);
        return Result.success();
    }


    //管理员同意后修改申请表的状态为1————0待审核 1已同意 2已拒绝
    //同意申请后向申请成功记录表添加一条新的记录
    @PatchMapping("/agreeApply")
    public Result agreeApply(@RequestHeader @NotNull Integer applicationid){
        if (!isAdmin()){
            return Result.error("无权限进行修改操作");
        }
        Adoptapply adoptapply = adoptapplyService.selectByApplyId(applicationid);
        adoptapply.setStatus("1");
        //更新申请表的状态为1 同意

        //如果申请成功的记录表中有的话就不用添加了
        if (adoptionrecordsService.queryByAnimalId(adoptapply.getAnimalid())==null){
            //把申请表中的记录添加到申请成功记录表中
            Adoptionrecords adoptionrecords = new Adoptionrecords(null, adoptapply.getAnimalid(), adoptapply.getUserid(), adoptapply.getApplicationdate(), null);
            adoptionrecordsService.addRecords(adoptionrecords);
        }else{
            //如果有记录则更新记录
            Adoptionrecords adoptionrecords = new Adoptionrecords(applicationid, adoptapply.getAnimalid(), adoptapply.getUserid(), adoptapply.getApplicationdate(), null);
            adoptionrecordsService.updateRecords(adoptionrecords);
        }
        //把动物的领养状态改为1已领养 并且更新动物表中的领养人id
        animalService.updateAdoptStatus(adoptapply.getStatus(),adoptapply.getAnimalid(),adoptapply.getUserid());

        adoptapplyService.updateApply(adoptapply);
        return Result.success("已同意领养，请尽快联系656457845进行现场领养");
    }

    //管理员拒绝后修改申请表的状态为2，并提供原因————0待审核 1已同意 2已拒绝
    @PatchMapping("/rejectApply")
    public Result rejectApply(@RequestParam @NotNull  Integer applicationid,@RequestParam @NotNull String rejectionreason){
        if (!isAdmin()){
            return Result.error("无权限进行修改操作");
        }
        //查询到这条申请表
        Adoptapply adoptapply = adoptapplyService.selectByApplyId(applicationid);
        //修改申请表的状态为2，并提供原因
        adoptapply.setRejectionreason(rejectionreason);
        adoptapply.setStatus("2");
        //如果记录表中有该动物的申请成功记录，则删除该成功记录
        if(adoptionrecordsService.queryByAnimalId(adoptapply.getAnimalid())!=null){
            Adoptionrecords adoptionrecords = adoptionrecordsService.queryByAnimalId(adoptapply.getAnimalid());
            Integer adoptionrecordid = adoptionrecords.getAdoptionrecordid();
            adoptionrecordsService.delRecords(adoptionrecordid);
        }

        //0未领养 1已领养 2待批准
        //把动物表中的领养状态改为0 并且更新动物表中的领养人id为null
        animalService.updateAdoptStatus("0",adoptapply.getAnimalid(),null);

        //然后修改申请表
        adoptapplyService.updateApply(adoptapply);
        return Result.success("已拒绝，原因如下："+rejectionreason);
    }


}

