package com.project.animal.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Carousel)表实体类
 *
 * @author makejava
 * @since 2024-04-30 00:39:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carousel  implements Serializable{
    
    private Integer carouselId;
    private String imageUrl;
    private Integer activityId;


}
