package com.project.animal.entity;

import java.util.Date;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Adoptapply)表实体类
 *
 * @author makejava
 * @since 2024-03-13 15:51:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Adoptapply  implements Serializable{
    
    /**
    * 申请ID
    */
    @NotNull(groups = {Update.class})
    private Integer applicationid;
    /**
    * 动物ID
    */
    @NotNull()
    private Integer animalid;
    /**
    * 用户ID
    */
    @NotNull()
    private Integer userid;
    /**
    * 申请日期
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applicationdate;
    /**
    * 申请状态
    */
    private String status;
    /**
    * 领养动机
    */
    @NotEmpty(groups = {Add.class})
    private String adoptionmotivation;
    /**
    * 家庭环境
    */
    @NotEmpty(groups = {Add.class})
    private String familyenvironment;
    /**
    * 个人经验
    */
    @NotEmpty(groups = {Add.class})
    private String personalexperience;
    /**
    * 附加信息
    */
    private String additionalinformation;
    /**
    * 拒绝理由
    */
//    @JsonIgnore
    private String rejectionreason;

    public interface Update extends Default {
        //更新时候需要的校验
    }

    //没有给add添加额外的，所有他全部的校验都会有
    //但是不会有已经增加了update的校验注解
    public interface Add extends Default {
        //添加时候需要的校验
    }

}
