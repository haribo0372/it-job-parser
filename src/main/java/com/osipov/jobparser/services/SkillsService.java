package com.osipov.jobparser.services;

import com.osipov.jobparser.models.Skill;
import com.osipov.jobparser.repositories.SkillsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillsService {
    @Autowired
    private SkillsRepository repository;

    public List<Skill> getAll(){
        return repository.findAll();
    }

    public void insert(Skill skill){
        if (!repository.findByName(skill.getName()).isEmpty()) return;

        repository.save(skill);
    }

    public void delete(Skill skill){
        repository.delete(skill);
    }
}
