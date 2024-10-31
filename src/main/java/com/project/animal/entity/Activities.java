package com.project.animal.entity;

import java.util.Date;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Activities)表实体类
 *
 * @author makejava
 * @since 2024-04-30 00:38:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activities  implements Serializable{
    
    private Integer activityId;

    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotNull
    private Integer leaderId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @NotEmpty
    private String type;
    @NotEmpty
    private String location;

    private Integer views;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @NotEmpty
    private Integer status;


}
