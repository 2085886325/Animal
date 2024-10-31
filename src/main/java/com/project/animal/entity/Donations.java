package com.project.animal.entity;

import java.util.Date;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Donations)表实体类
 *
 * @author makejava
 * @since 2024-03-14 23:24:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Donations  implements Serializable{

    public interface Update extends Default {
    }
    public interface Add extends Default {
    }
    /**
    * 捐赠ID
    */
    @NotNull(groups = Update.class)
    private Integer donationid;
    /**
    * 用户ID
    */
    private Integer userId;
    /**
    * 金额
    */
    @NotNull(groups = Add.class)
    private Double amount;
    /**
    * 捐赠日期
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date donationdate;
    /**
    * 捐赠类型
    */
    private String donationtype;
    /**
    * 描述
    */
    private String description;


}
