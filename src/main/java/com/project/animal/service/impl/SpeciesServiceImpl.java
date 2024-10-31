package com.project.animal.service.impl;

import com.project.animal.entity.Animals;
import com.project.animal.entity.Species;
import com.project.animal.mapper.SpeciesMapper;
import com.project.animal.service.ISpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rhy
 * @since 2024-03-04
 */
@Service
public class SpeciesServiceImpl implements ISpeciesService {
    @Autowired
    private SpeciesMapper speciesMapper;

    @Override
    public void addSpecies(String species) {
        speciesMapper.addSpecies(species);
    }

    @Override
    public void deleteSpecies(Integer speciesId) {
        speciesMapper.deleteSpecies(speciesId);
    }

    @Override
    public void updateSpecies(Species species) {
        speciesMapper.updateSpecies(species);
    }

    @Override
    public Species findSpeciesById(Integer speciesId) {
        return speciesMapper.findSpeciesById(speciesId);
    }

    @Override
    public List<Species> list() {
        return speciesMapper.list();
    }

    @Override
    public Integer listBySpeciesName(String speciesName) {
        return speciesMapper.listBySpeciesName(speciesName);
    }
}
