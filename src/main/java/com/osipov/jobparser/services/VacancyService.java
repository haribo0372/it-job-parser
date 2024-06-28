package com.osipov.jobparser.services;

import com.osipov.jobparser.models.City;
import com.osipov.jobparser.models.Profession;
import com.osipov.jobparser.models.Skill;
import com.osipov.jobparser.models.Vacancy;
import com.osipov.jobparser.repositories.CityRepository;
import com.osipov.jobparser.repositories.ProfessionRepository;
import com.osipov.jobparser.repositories.SkillRepository;
import com.osipov.jobparser.repositories.VacancyRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VacancyService {
    private final VacancyRepository vacancyRepository;
    private final ProfessionRepository professionRepository;
    private final CityRepository cityRepository;
    private final SkillRepository skillRepository;

    @Autowired
    public VacancyService(VacancyRepository repository,
                          ProfessionRepository professionRepository, CityRepository cityRepository,
                          SkillRepository skillRepository) {
        this.vacancyRepository = repository;
        this.professionRepository = professionRepository;
        this.cityRepository = cityRepository;
        this.skillRepository = skillRepository;
    }

    public List<Vacancy> getAll() {
        return vacancyRepository.findAll();
    }

    @Transactional
    public void save(Vacancy vacancy) {
        if (vacancyRepository.findByUrl(vacancy.getUrl()).isPresent()) return;

        Optional<City> cityOptional = cityRepository.findByName(vacancy.getCity().getName());
        cityOptional.ifPresent(vacancy::setCity);

        Set<Skill> managedSkills = new HashSet<>();
        for (Skill vacancySkill : vacancy.getSkills()) {
            Optional<Skill> existingSkill = skillRepository.findByName(vacancySkill.getName());
            if (existingSkill.isPresent()) {
                managedSkills.add(existingSkill.get());
            } else {
                managedSkills.add(vacancySkill);
            }
        }
        vacancy.setSkills(managedSkills);
        vacancyRepository.save(vacancy);
    }

    public void save(List<Vacancy> vacancies) {
        vacancies.forEach(this::save);
    }

    public void delete(Vacancy vacancy) {
        vacancyRepository.delete(vacancy);
    }

    public Vacancy createVacancy() {
        return new Vacancy();
    }

    public List<Vacancy> vacancyFiltering(Vacancy vacancy){

        return List.of();
    }

    @PersistenceContext
    private EntityManager entityManager;

    public List<Vacancy> findVacanciesByCriteria(Vacancy vacancy) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vacancy> query = cb.createQuery(Vacancy.class);
        Root<Vacancy> root = query.from(Vacancy.class);

        List<Predicate> predicates = new ArrayList<>();

        if (vacancy.getCity() != null && !vacancy.getCity().getName().isEmpty()) {
            predicates.add(cb.equal(root.get("city_id"), vacancy.getId()));
        }
        if (vacancy.getCompany() != null && !vacancy.getCompany().isEmpty()) {
            predicates.add(cb.equal(root.get("company"), vacancy.getCompany()));
        }
        if (vacancy.getTitle() != null && !vacancy.getTitle().isEmpty()) {
            predicates.add(cb.equal(root.get("title"), vacancy.getTitle()));
        }

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }
}
