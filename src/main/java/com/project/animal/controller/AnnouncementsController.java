package com.project.animal.controller;



import com.project.animal.entity.Announcements;
import com.project.animal.entity.NoticeDTO;
import com.project.animal.entity.Result;
import com.project.animal.service.AnnouncementsService;
import com.project.animal.service.IUsersService;
import com.project.animal.utils.ThreadLocalUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * (Announcements)表控制层
 *
 * @author makejava
 * @since 2024-03-14 15:26:01
 */
@RestController
@RequestMapping("/notice")
public class AnnouncementsController{
    /**
     * 服务对象
     */
    @Autowired
    private AnnouncementsService announcementsService;

    @Autowired
    private IUsersService usersService;


    //首先判断是不是管理员
    public boolean isAdmin(){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userid = (Integer) map.get("id");
        //根据userid查询用户是否是管理员
        boolean isadmin = usersService.isAdmin(userid);
        //如果isadmin==0，说明不是管理员，不可以修改
        return isadmin;
    }


    /**
     * 通过ID查询单条数据
     *
     * @param announcementId 主键
     * @return 实例对象
     */
    @GetMapping("/selectById")
    public Result<NoticeDTO> selectById(@RequestParam Integer announcementId){
        NoticeDTO announcements = announcementsService.selectById(announcementId);
        return Result.success(announcements);
    }

    /**
     * 查询所有数据
     *
     * @return 对象列表
     */
    @GetMapping("/selectAll")
    public Result<List<NoticeDTO>> selectAll(){
        if (!isAdmin()){
            return Result.error("对不起，您没有权限查看全部的公告");
        }
        List<NoticeDTO> announcements = announcementsService.selectAll();
        return Result.success(announcements);
    }

    //查询发布状态为已经发布的公告
    @GetMapping("/selectByStatus")
    public Result<List<Announcements>> selectByStatus(){
        List<Announcements> announcements = announcementsService.selectByStatus();
        return Result.success(announcements);
    }


    @PostMapping("/deleteAnnouncement")
    public Result deleteAnnouncement(@RequestParam Integer announcementId){
        //首先判断是不是管理员
        if(!isAdmin()){
            return Result.error("对不起，您没有权限删除公告");
        }
        //调用service删除
        announcementsService.deleteAnnouncement(announcementId);
        return Result.success();
    }

    //新增公告
    @PostMapping("/addAnnouncement")
    public Result addAnnouncement(@RequestBody @Validated Announcements announcements){
        //首先判断是不是管理员
        if(!isAdmin()){
            return Result.error("对不起，您没有权限添加公告");
        }
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userid = (Integer) map.get("id");
        announcements.setPublisherId(userid);
        //announcements.setStatus("draft");已经添加自定义的参数校验注解
        //调用service添加
        announcementsService.addAnnouncement(announcements);
        return Result.success(announcements);
    }

    //修改公告
    @PostMapping("/updateAnnouncement")
    public Result updateAnnouncement(@RequestBody @Validated(Announcements.Update.class) Announcements announcements){
        //首先判断是不是管理员
        if(!isAdmin()){
            return Result.error("对不起，您没有权限修改公告");
        }
        //调用service修改
        announcementsService.updateAnnouncement(announcements);
        return Result.success(announcements);
    }


}

