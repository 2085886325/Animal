package com.project.animal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.animal.anno.State;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO  implements Serializable {
    private String username;
    /**
     * 公告id
     */
    private Integer announcementId;
    /**
     * 标题
     */

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
    private Integer publisherId;
    /**
     * 状态
     */
    private String status;


}
