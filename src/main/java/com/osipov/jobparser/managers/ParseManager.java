package com.osipov.jobparser.managers;

import com.osipov.jobparser.models.Profession;
import com.osipov.jobparser.models.Vacancy;
import com.osipov.jobparser.parsers.HHParser;
import com.osipov.jobparser.parsers.HabrCareerParser;
import com.osipov.jobparser.parsers.Parser;
import com.osipov.jobparser.repositories.ProfessionRepository;
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
    private final HabrCareerParser habrCareerParser;
    private final ProfessionRepository professionRepository;
    private final VacancyService vacancyService;

    @Autowired
    public ParseManager(HHParser hhParser, HabrCareerParser habrCareerParser, ProfessionRepository professionRepository, VacancyService vacancyService) {
        this.hhParser = hhParser;
        this.habrCareerParser = habrCareerParser;
        this.professionRepository = professionRepository;
        this.vacancyService = vacancyService;
    }

    public void fillVacancy() {
        List<Profession> allProfession = this.professionRepository.findAll();
        for (Profession profession : allProfession) {
            try {
                this.vacancyService.save(this.hhParser.parse(profession));
                this.vacancyService.save(habrCareerParser.parse(profession));
            } catch (IOException ignored) {
            } catch (NonUniqueResultException | IncorrectResultSizeDataAccessException uniqueResultException) {
                System.out.println(uniqueResultException.getMessage());
            }
        }
    }
}
