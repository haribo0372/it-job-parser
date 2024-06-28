package com.osipov.jobparser.managers;

import com.osipov.jobparser.models.Profession;
import com.osipov.jobparser.models.Vacancy;
import com.osipov.jobparser.parsers.HHParser;
import com.osipov.jobparser.repositories.ProfessionRepository;
import com.osipov.jobparser.repositories.SkillRepository;
import com.osipov.jobparser.services.VacancyService;
import org.hibernate.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component("parseManager")
public class ParseManager {
    private final HHParser hhParser;
    private final ProfessionRepository professionRepository;
    private final VacancyService vacancyService;

    @Autowired
    public ParseManager(HHParser hhParser, ProfessionRepository professionRepository, VacancyService vacancyService) {
        this.hhParser = hhParser;
        this.professionRepository = professionRepository;
        this.vacancyService = vacancyService;

//        if (vacancyService.getAll().size() < 10) {
//            List<Profession> allProfession = this.professionRepository.findAll();
//            for (Profession profession : allProfession) {
//                try {
//                    List<Vacancy> vacancies = this.hhParser.parse(profession);
//                    for (Vacancy vacancy : vacancies) {
//                        this.vacancyService.save(vacancy);
//                    }
//                } catch (IOException ignored) {
//                } catch (NonUniqueResultException | IncorrectResultSizeDataAccessException uniqueResultException) {
//                    System.out.println(uniqueResultException.getMessage());
//                }
//            }
//        }
    }

    public void createDB(){
        List<Profession> allProfession = this.professionRepository.findAll();
        for (Profession profession : allProfession) {
            try {
                List<Vacancy> vacancies = this.hhParser.parse(profession);
                for (Vacancy vacancy : vacancies){
                    this.vacancyService.save(vacancy);
                }
            } catch (IOException ignored){ }
            catch (NonUniqueResultException | IncorrectResultSizeDataAccessException uniqueResultException){
                System.out.println(uniqueResultException.getMessage());
            }
        }
    }
}
