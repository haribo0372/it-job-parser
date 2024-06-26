package com.osipov.jobparser.services;

import com.osipov.jobparser.models.Skill;
import com.osipov.jobparser.models.Vacancy;
import com.osipov.jobparser.repositories.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VacancyService {
    @Autowired
    private VacancyRepository repository;

    public List<Vacancy> getAll(){
        return repository.findAll();
    }

    public void insert(Vacancy vacancy){
        if (!repository.findByUrl(vacancy.getUrl()).isEmpty()) return;

        repository.save(vacancy);
    }

    public void delete(Vacancy vacancy){
        repository.delete(vacancy);
    }
}
