package com.osipov.jobparser.controllers;

import com.osipov.jobparser.models.City;
import com.osipov.jobparser.models.Vacancy;
import com.osipov.jobparser.repositories.CityRepository;
import com.osipov.jobparser.repositories.ProfessionRepository;
import com.osipov.jobparser.repositories.SkillRepository;
import com.osipov.jobparser.services.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/search/job")
public class JobController {
    private VacancyService vacancyService;
    private ProfessionRepository professionRepository;
    private SkillRepository skillRepository;
    private CityRepository cityRepository;

    @Autowired
    public JobController(VacancyService vacancyService, ProfessionRepository professionRepository,
                         SkillRepository skillRepository, CityRepository cityRepository) {
        this.vacancyService = vacancyService;
        this.professionRepository = professionRepository;
        this.skillRepository = skillRepository;
        this.cityRepository = cityRepository;
    }

    @GetMapping
    public String getMain(Model model){
        model.addAttribute("cities", cityRepository.findAll());
        model.addAttribute("professions", professionRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        model.addAttribute("vacancyForm", vacancyService.createVacancy());
        return "vacancy/select";
    }

    @PostMapping
    public String postMain(@ModelAttribute("vacancyForm")Vacancy vacancy){
        System.out.println(vacancy);
        return "redirect:/search/job";
    }

}
