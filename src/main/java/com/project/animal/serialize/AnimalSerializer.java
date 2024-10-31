package com.project.animal.serialize;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.project.animal.entity.Animals;
import com.project.animal.entity.Species;
import com.project.animal.entity.Users;
import com.project.animal.service.ISpeciesService;
import com.project.animal.service.IUsersService;
import com.project.animal.service.SheltersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.IOException;
//过滤掉返回值为null的字段
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class AnimalSerializer extends JsonSerializer<Animals> {
    @Autowired
    private ISpeciesService speciesService;

    @Autowired
    private SheltersService sheltersService;


    @Autowired
    private IUsersService userService;

    @Override
    public void serialize(Animals animal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("animalId", animal.getAnimalid());
        jsonGenerator.writeStringField("name", animal.getName());
        // 其他属性的序列化
        jsonGenerator.writeStringField("description", animal.getDescription());
        jsonGenerator.writeStringField("picture", animal.getPicture());
        jsonGenerator.writeStringField("gender", animal.getGender());
        if (animal.getAge()!=null){
            jsonGenerator.writeNumberField("age", animal.getAge());
        }
        if (animal.getWeight()!=null){
            jsonGenerator.writeNumberField("weight", animal.getWeight());
        }

        jsonGenerator.writeStringField("healthStatus", animal.getHealthstatus());
        jsonGenerator.writeStringField("status", animal.getStatus());

        if (animal.getAdopterid()!=null){
            //通过收养人的id查询收养人名称 调用方法
            Integer adopterId = animal.getAdopterid();
            jsonGenerator.writeNumberField("adopterid", adopterId);
            Users adopter = userService.findByUserId(adopterId);
            jsonGenerator.writeStringField("adopterName", adopter.getUsername());

        }

        // 在序列化时获取物种名称，并注入到 JSON 中
        Integer speciesId = animal.getSpeciesid();
        Species species = speciesService.findSpeciesById(speciesId); // 这个方法可以根据ID获取物种名称

        jsonGenerator.writeStringField("speciesName", species.getSpeciesName());
        jsonGenerator.writeNumberField("speciesId", speciesId);
        if (animal.getShelterid()!=null){
            //对收养所获取名称，并注入到JSON中
            jsonGenerator.writeStringField("shelterName", sheltersService.getShById(animal.getShelterid()).getSheltername());
        }

        jsonGenerator.writeEndObject();
    }
}
