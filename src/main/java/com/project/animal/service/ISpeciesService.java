package com.project.animal.service;

import com.project.animal.entity.Animals;
import com.project.animal.entity.Species;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rhy
 * @since 2024-03-04
 */
public interface ISpeciesService {

    void addSpecies(String speciesName);

    void deleteSpecies(Integer speciesId);

    void updateSpecies(Species species);

    Species findSpeciesById(Integer speciesId);

    List<Species> list();

    Integer listBySpeciesName(String speciesName);
}
