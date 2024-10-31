package com.project.animal.controller;



import com.project.animal.entity.Donations;
import com.project.animal.entity.Result;
import com.project.animal.service.DonationsService;
import com.project.animal.service.IUsersService;
import com.project.animal.utils.ThreadLocalUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * (Donations)表控制层
 *
 * @author makejava
 * @since 2024-03-14 23:24:46
 */
@RestController
@RequestMapping("/donations")
public class DonationsController{
    /**
     * 服务对象
     */
    @Autowired
    private DonationsService donationsService;


    @Autowired
    private IUsersService usersService;

    //查询全部捐赠记录
    @GetMapping("/selectAll")
    public Result<List<Donations>> selectAll(){
        List<Donations> donations = donationsService.selectAll();
        return Result.success(donations);
    }

    //通过用户id查询该用户的捐赠记录
    @GetMapping("/selectByUserId")
    public Result<List<Donations>> selectByUserId(@RequestHeader Integer userid){
        List<Donations> donations = donationsService.selectByUserId(userid);
        return Result.success(donations);
    }


    //通过捐赠id查询捐赠的详情
    @GetMapping("/selectById")
    public Result<Donations> selectByDoId(@RequestHeader Integer donationid){
        Donations donations = donationsService.selectByDoId(donationid);
        return Result.success(donations);
    }

    //用户捐赠，新增一条记录
    @PostMapping("/addDonation")
    public Result addDonation(@RequestBody @Validated(Donations.Add.class) Donations donations){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        donations.setUserId(userId);
//        donations.setDonationdate(new java.util.Date());
//        donations.setDonationdate(LocalDateTime.now());
        donationsService.addDonation(donations);
        return Result.success();
    }


    //删除捐赠，管理员权限才可以
    //首先要判断是不是管理员
    public boolean isAdmin(){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userid = (Integer) map.get("id");
        //根据userid查询用户是否是管理员
        boolean isadmin = usersService.isAdmin(userid);
        //如果isadmin==0，说明不是管理员，不可以修改
        return isadmin;
    }
    @PostMapping("/deleteDonation")
    public Result deleteDonation(@RequestHeader Integer donationid){
        if(!isAdmin()){
            return Result.error("对不起，您没有权限删除捐赠记录");
        }
        donationsService.deleteDonation(donationid);
        return Result.success();
    }

    //修改捐赠记录，管理员权限才可以
    @PostMapping("/updateDonation")
    public Result updateDonation(@RequestBody @Validated(Donations.Update.class) Donations donations){
        if(!isAdmin()){
            return Result.error("对不起，您没有权限修改捐赠记录");
        }
        donationsService.updateDonation(donations);
        return Result.success();
    }

}

