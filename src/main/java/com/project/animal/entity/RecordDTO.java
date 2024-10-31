package com.project.animal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordDTO  implements Serializable {
    private String username;

    private String name;
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
