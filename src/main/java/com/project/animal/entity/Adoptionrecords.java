package com.project.animal.entity;

import java.util.Date;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Adoptionrecords)表实体类
 *
 * @author makejava
 * @since 2024-03-09 12:04:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Adoptionrecords  implements Serializable{
    
    /**
    * 领养记录ID
    */
    private Integer adoptionrecordid;
    /**
    * 动物ID
    */
    private Integer animalid;
    /**
    * 用户ID
    */
    private Integer userid;
    /**
    * 申请日期
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applicationdate;
    /**
    * 领养日期
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date adoptiondate;


}
