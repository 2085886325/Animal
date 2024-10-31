package com.project.animal.mapper;

import com.project.animal.entity.Species;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author rhy
 * @since 2024-03-04
 */
@Mapper
public interface SpeciesMapper {
    @Insert("insert into species(speciesname) values(#{speciesName})")
    void addSpecies(String speciesName);

    @Delete("delete from species where speciesid=#{speciesId}")
    void deleteSpecies(Integer speciesId);

    @Update("update species set speciesname=#{speciesName} where speciesid=#{speciesId}")
    void updateSpecies(Species species);

    @Select("select * from species where speciesid=#{speciesId}")
    Species findSpeciesById(Integer speciesId);

    @Select("select * from species")
    List<Species> list();

    @Select("select speciesid from species where speciesname=#{speciesName}")
    Integer listBySpeciesName(String speciesName);

}
