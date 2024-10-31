package com.project.animal.controller;



import com.project.animal.entity.Activities;
import com.project.animal.entity.ActivityDTO;
import com.project.animal.entity.Carousel;
import com.project.animal.entity.Result;
import com.project.animal.service.ActivitiesService;
import com.project.animal.service.CarouselService;
import com.project.animal.service.IUsersService;
import com.project.animal.utils.ThreadLocalUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * (Activities)表控制层
 *
 * @author makejava
 * @since 2024-04-30 00:38:45
 */
@RestController
@RequestMapping("/activities")
public class ActivitiesController{
    /**
     * 服务对象
     */
    @Autowired
    private ActivitiesService activitiesService;

    @Autowired
    private IUsersService usersService;

    @Autowired
    private CarouselService carouselService;

    //首先要判断是不是管理员
    public boolean isAdmin(){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userid = (Integer) map.get("id");
        //根据userid查询用户是否是管理员
        //如果isadmin==0，说明不是管理员，不可以修改
        return usersService.isAdmin(userid);
    }
    //查询全部活动
    @GetMapping("/queryAll")
    public Result<List<ActivityDTO>> queryAll(){
        if (!isAdmin()){
            return Result.error("非管理员无权限查询");
        }
        return Result.success(activitiesService.queryAll());
    }

    //通过活动id查询活动
    @GetMapping("/queryById")
    public Result<Activities> queryById( @RequestParam Integer activityId){
        Activities activity = activitiesService.queryById(activityId);
        if (activity==null){
            return Result.error("活动不存在");
        }
        //每查询一次活动的浏览次数加一
        activity.setViews(activity.getViews()+1);
        //查询活动时间是否已经过期
        if (activity.getEndTime().before(new java.util.Date())){
            activity.setStatus(0);
        }else{
            activity.setStatus(1);
        }
        activitiesService.update(activity);
        return Result.success(activity);
    }

    //添加活动
    @PostMapping("/insert")
    public Result<Activities> insert(@RequestBody Activities activities){
        if (!isAdmin()){
            return Result.error("非管理员无权限添加");
        }
        activitiesService.insert(activities);
        return Result.success();
    }


    //修改活动
    @PostMapping("/update")
    public Result<Activities> update(@RequestBody Activities activities){
        if (!isAdmin()){
            return Result.error("非管理员无权限修改");
        }
        //如果活动正在轮播图中使用，则不能修改
//        if(activities.getStatus()==0){
//            Carousel carousel = carouselService.queryById(activities.getActivityId());
//            if (carousel!=null){
//                return Result.error("活动正在轮播图中使用，不能修改状态");
//            }
//        }
        activitiesService.update(activities);
        return Result.success();
    }

    //删除活动
    @PostMapping("/deleteById")
    public Result<Activities> deleteById( @RequestParam Integer activityId){
        if (!isAdmin()){
            return Result.error("非管理员无权限删除");
        }
        Carousel carousel = carouselService.queryById(activityId);
        if (carousel!=null){
            return Result.error("活动正在轮播图中使用，不能删除");
        }
        activitiesService.deleteById(activityId);
        return Result.success();
    }


}

