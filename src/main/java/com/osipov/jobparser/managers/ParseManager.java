package com.osipov.jobparser.managers;

import com.osipov.jobparser.models.Profession;
import com.osipov.jobparser.models.Vacancy;
import com.osipov.jobparser.parsers.HHParser;
import com.osipov.jobparser.parsers.HabrCareerParser;
import com.osipov.jobparser.repositories.ProfessionRepository;
import com.osipov.jobparser.services.VacancyService;
import org.hibernate.NonUniqueResultException;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
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
                this.vacancyService.save(hhParser.parse(profession));
                this.vacancyService.save(habrCareerParser.parse(profession));
            } catch (IOException ignored) {
            } catch (NonUniqueResultException | IncorrectResultSizeDataAccessException uniqueResultException) {
                System.out.println(uniqueResultException.getMessage());
            }
        }
    }

    public void fillVacancy(Profession profession){
        try {
            this.vacancyService.save(hhParser.parse(profession));
            this.vacancyService.save(habrCareerParser.parse(profession));
        } catch (IOException ignored) {
        } catch (NonUniqueResultException | IncorrectResultSizeDataAccessException uniqueResultException) {
            System.out.println(uniqueResultException.getMessage());
        }
    }

    @Scheduled(fixedRate = 10800000)
    public void checkLinkValidity() {
        for (Vacancy vacancy : vacancyService.getVacancies()) {
            try {
                Document doc = habrCareerParser.getHtmlCode(vacancy.getUrl());

                if (!doc.select("p.vacancy-archive-description").isEmpty() ||
                        doc.select("div.no-content__title").text().equals("Вакансия в архиве")) {
                    vacancyService.delete(vacancy);
                }
            } catch (IOException e) {
                vacancyService.delete(vacancy);
            }
        }

        if (vacancyService.count() < 300) fillVacancy();
    }
}
