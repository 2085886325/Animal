package com.project.animal.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Shelters)表实体类
 *
 * @author makejava
 * @since 2024-03-18 23:29:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shelters  implements Serializable{
    
    /**
    * 收容所ID
    */
    private Integer shelterid;
    /**
    * 收容所名称
    */
    private String sheltername;
    /**
    * 位置
    */
    private String shelteraddress;
    /**
    * 联系方式
    */
    private String shelterphone;
    /**
    * 介绍
    */
    private String introduce;


}
