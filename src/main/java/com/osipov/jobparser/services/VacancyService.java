package com.osipov.jobparser.services;

import com.osipov.jobparser.models.City;
import com.osipov.jobparser.models.Skill;
import com.osipov.jobparser.models.Vacancy;
import com.osipov.jobparser.repositories.CityRepository;
import com.osipov.jobparser.repositories.ProfessionRepository;
import com.osipov.jobparser.repositories.SkillRepository;
import com.osipov.jobparser.repositories.VacancyRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class VacancyService {
    @PersistenceContext
    private EntityManager entityManager;

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

    public List<Vacancy> getVacancies() {
        return vacancyRepository.findAll();
    }

    public List<Vacancy> getVacancies(Integer max) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vacancy> cq = cb.createQuery(Vacancy.class);
        Root<Vacancy> root = cq.from(Vacancy.class);
        cq.select(root);

        jakarta.persistence.TypedQuery<Vacancy> query = entityManager.createQuery(cq);
        query.setMaxResults(max);

        return query.getResultList();
    }

    public Optional<Vacancy> findById(Long id) {
        return vacancyRepository.findById(id);
    }

    @Transactional
    public void save(Vacancy vacancy) {
        if (vacancyRepository.findByUrl(vacancy.getUrl()).isPresent()) return;

        Optional<City> cityOptional = cityRepository.findByName(vacancy.getCity().getName());
        cityOptional.ifPresent(vacancy::setCity);

        Set<Skill> managedSkills = new HashSet<>();
        vacancy.getSkills().forEach(vacancySkill -> {
            Optional<Skill> existingSkill = skillRepository.findByName(vacancySkill.getName());
            if (existingSkill.isPresent()) {
                managedSkills.add(existingSkill.get());
            } else {
                managedSkills.add(vacancySkill);
            }
        });

        vacancy.setSkills(managedSkills);
        vacancyRepository.save(vacancy);
    }

    @Transactional
    public void save(Collection<Vacancy> vacancies) {
        vacancies.forEach(this::save);
    }

    @Transactional
    public void delete(Vacancy vacancy) {
        Optional<Vacancy> vacancyOptional = vacancyRepository.findById(vacancy.getId());
        vacancyOptional.ifPresent(i -> {
            Vacancy currentVacancy = vacancyOptional.get();
            currentVacancy.getUsers().forEach(user -> user.removeVacancy(vacancy));
            currentVacancy.getUsers().clear();
            vacancyRepository.delete(currentVacancy);
        });
    }

    public void deleteAll() {
        vacancyRepository.findAll().forEach(this::delete);
    }

    public long count() {
        return vacancyRepository.count();
    }

    public Vacancy createVacancy() {
        return new Vacancy();
    }

    public List<Vacancy> findVacanciesByParams(Vacancy vacancy) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vacancy> query = cb.createQuery(Vacancy.class);
        Root<Vacancy> root = query.from(Vacancy.class);
        Join<Vacancy, Skill> skillsJoin = root.join("skills", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (vacancy.getCity() != null && !vacancy.getCity().getName().isEmpty()) {
            predicates.add(cb.equal(root.get("city"), vacancy.getCity()));
        }
        if (vacancy.getProfession() != null && !vacancy.getProfession().getName().isEmpty()) {
            predicates.add(cb.equal(root.get("profession"), vacancy.getProfession()));
        }

        Set<Skill> currentSkills = vacancy.getSkills();
        if (currentSkills != null && !currentSkills.isEmpty()) {
            List<Predicate> skillPredicates = new ArrayList<>();
            currentSkills.forEach(skill -> {
                skillPredicates.add(cb.isMember(skill, root.get("skills")));
            });
            predicates.add(cb.and(skillPredicates.toArray(new Predicate[0])));
        }

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }
}
