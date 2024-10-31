package com.project.animal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarouselDTO  implements Serializable {

    private Integer carouselId;
    private String imageUrl;
    private Integer activityId;
    private String title;


}