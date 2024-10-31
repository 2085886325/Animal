package com.project.animal.entity;

import java.util.Date;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.animal.anno.State;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Announcements)表实体类
 *
 * @author makejava
 * @since 2024-03-14 19:33:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Announcements  implements Serializable{
    public interface Update extends Default {

    }

    
    /**
    * 公告id
    */
    @NotNull(groups = Update.class)
    private Integer announcementId;
    /**
    * 标题
    */
    @NotEmpty
    private String title;
    /**
    * 内容
    */
    private String content;
    /**
    * 发布日期
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;
    /**
    * 发布者
    */
    @NotNull
    private Integer publisherId;
    /**
    * 状态
    */
    @State
    private String status;


}
