package com.project.animal.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.animal.anno.AnimalsStatus;
import com.project.animal.serialize.AnimalSerializer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (Animals)实体类
 *
 * @author makejava
 * @since 2024-03-07 20:29:48
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = AnimalSerializer.class)//对象序列化，为了让物种类别直接显示文字而不是数字
@JsonInclude(JsonInclude.Include.ALWAYS) // 始终包含字段，包括空值
// 二级缓存的实体类必须实现Serializable接口
public class Animals implements Serializable {
    //分组校验 如果说某个较验项没有指定分组，默认属于Default分组
    //分组之间可以推承，A extends B，那么A中拥有B中所有的校验项
    public interface Update extends Default {
        //更新动物时候需要的校验
    }

    //没有给add添加额外的，所有他全部的校验都会有
    //但是不会有已经增加了update的校验注解
    public interface Add extends Default {
        //添加动物时候需要的校验
    }

    public interface Delete {
        //删除动物时候需要的校验
    }
    private static final long serialVersionUID = 925480073389449283L;
    /**
     * 动物ID
     */

    @NotNull(groups = Update.class)
    private Integer animalid;
    /**
     * 物种ID
     */
    @NotNull
    private Integer speciesid;
    /**
     * 姓名
     */
    @NotEmpty
    private String name;
    /**
     * 描述或特征
     */
    private String description;
    /**
     * 性别
     */
    @NotEmpty
    private String gender;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 体重
     */
    private Double weight;
    /**
     * 健康状况
     */
    private String healthstatus;
    /**
     * 图片
     */
    private String picture;
    /**
     * 收容所ID
     */
    private Integer shelterid;
    /**
     * 状态
     */
    @AnimalsStatus
    private String status;
    /**
     * 领养人ID
     */
    private Integer adopterid;

}

