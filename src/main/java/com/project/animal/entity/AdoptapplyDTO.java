package com.project.animal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdoptapplyDTO  implements Serializable {
    private String username;
    private String name;
    private Integer applicationid;

    private Integer animalid;

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

    private String adoptionmotivation;

    private String familyenvironment;

    private String personalexperience;

    private String additionalinformation;

    private String rejectionreason;
}
