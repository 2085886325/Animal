package com.project.animal.controller;



import com.project.animal.entity.Activities;
import com.project.animal.entity.Carousel;
import com.project.animal.entity.CarouselDTO;
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
 * (Carousel)表控制层
 *
 * @author makejava
 * @since 2024-04-30 00:39:16
 */
@RestController
@RequestMapping("/carousel")
public class CarouselController{
    /**
     * 服务对象
     */
    @Autowired
    private CarouselService carouselService;

    @Autowired
    private IUsersService usersService;

    @Autowired
    private ActivitiesService activitiesService;

    //首先要判断是不是管理员
    public boolean isAdmin(){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userid = (Integer) map.get("id");
        //根据userid查询用户是否是管理员
        boolean isadmin = usersService.isAdmin(userid);
        //如果isadmin==0，说明不是管理员，不可以修改
        return isadmin;
    }

    //查询全部的轮播图
    @GetMapping("/queryAll")
    public Result<List<CarouselDTO>> queryAll(){
        return Result.success(carouselService.queryAll());
    }

    //修改轮播图
    @PostMapping ("/update")
    public Result<Carousel> update(@RequestBody  Carousel carousel){
        //判断是不是管理员
        if(!isAdmin()){
            return Result.error("非管理员无法修改");
        }
        //判断传入的活动id查询到该活动是否存在，并且查询到的活动状态是否不是已结束
        Activities activities = activitiesService.queryById(carousel.getActivityId());
//        System.out.println(activities);
        if(activities==null||activities.getStatus().toString().equals("0")){
            return Result.error("该活动已结束或者不存在");
        }
        //修改轮播图
        carouselService.update(carousel);
        return Result.success();
    }



}

