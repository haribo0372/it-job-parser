package com.osipov.jobparser.controllers;

import com.osipov.jobparser.models.User;
import com.osipov.jobparser.models.Vacancy;
import com.osipov.jobparser.repositories.CityRepository;
import com.osipov.jobparser.repositories.ProfessionRepository;
import com.osipov.jobparser.repositories.SkillRepository;
import com.osipov.jobparser.services.UserService;
import com.osipov.jobparser.services.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/search")
public class JobController {
    private final VacancyService vacancyService;
    private final ProfessionRepository professionRepository;
    private final SkillRepository skillRepository;
    private final CityRepository cityRepository;


    @Autowired
    public JobController(VacancyService vacancyService, ProfessionRepository professionRepository,
                         SkillRepository skillRepository, CityRepository cityRepository) {
        this.vacancyService = vacancyService;
        this.professionRepository = professionRepository;
        this.skillRepository = skillRepository;
        this.cityRepository = cityRepository;

    }

    @GetMapping
    public String getMain(Model model) {
        fillModelParams(model);
        if (!model.containsAttribute("result"))
            model.addAttribute("results", vacancyService.getVacancies(30));

        return "vacancy/select";
    }

    @PostMapping
    public String postMain(@ModelAttribute("vacancyForm") Vacancy vacancy, Model model) {
        fillModelParams(model);
        model.addAttribute("results", vacancyService.findVacanciesByParams(vacancy));

        return "vacancy/select";
    }


    private void fillModelParams(Model model) {
        model.addAttribute("cities", cityRepository.findAll());
        model.addAttribute("professions", professionRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        model.addAttribute("vacancyForm", vacancyService.createVacancy());
        model.addAttribute("isAuthenticated", UserService.isUserAuthenticated());
        model.addAttribute("vacancyService", vacancyService);
    }
}
