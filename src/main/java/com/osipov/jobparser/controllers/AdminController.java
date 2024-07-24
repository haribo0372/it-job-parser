package com.osipov.jobparser.controllers;

import com.osipov.jobparser.managers.ParseManager;
import com.osipov.jobparser.models.Profession;
import com.osipov.jobparser.models.User;
import com.osipov.jobparser.models.Vacancy;
import com.osipov.jobparser.repositories.ProfessionRepository;
import com.osipov.jobparser.services.UserService;
import com.osipov.jobparser.services.VacancyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final ProfessionRepository professionRepository;
    private final ParseManager parseManager;
    private final VacancyService vacancyService;

    @Autowired
    public AdminController(UserService userService, ProfessionRepository professionRepository, ParseManager parseManager, VacancyService vacancyService) {
        this.userService = userService;
        this.professionRepository = professionRepository;
        this.parseManager = parseManager;
        this.vacancyService = vacancyService;
    }

    @GetMapping
    public String userList() {
        return "admin/main_admin";
    }

    // User
    @GetMapping("/user")
    public String userPage(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        model.addAttribute("userForm", new User());
        return "admin/user";
    }

    @PostMapping("/user/insert")
    public String insertUser(@ModelAttribute("userForm") @Valid User user, BindingResult bindingResult){
        if (bindingResult.hasErrors()) return "admin/user";
        if (!userService.saveUser(user)) {
            System.out.println("Не удалось сохранить пользователя");
        }
        return "redirect:/admin/user";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Long userID) {
        userService.deleteUser(userID);
        return "redirect:/admin/user";
    }

    // Profession
    @GetMapping("/profession")
    public String professionPage(Model model) {
        model.addAttribute("allProfessions", professionRepository.findAll());
        model.addAttribute("professionForm", new Profession());
        return "admin/profession";
    }

    @PostMapping("/profession/insert")
    public String insertProfession(@ModelAttribute("professionForm") @Valid Profession profession,
                                   BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allProfessions", professionRepository.findAll());
            return "admin/profession";
        }
        Optional<Profession> professionOptional = professionRepository.findByName(profession.getName());
        if (professionOptional.isEmpty()) professionRepository.save(profession);
        return "redirect:/admin/profession";
    }

    @GetMapping("/profession/delete/{id}")
    public String deleteProf(@PathVariable("id") Long id) {
        Optional<Profession> professionOptional = professionRepository.findById(id);
        if (professionOptional.isPresent()){
            Profession profession = professionOptional.get();
            Set<Vacancy> vacancies = profession.getVacancies();

            for (Vacancy vacancy : vacancies){
                vacancyService.delete(vacancy);
            }
            professionRepository.delete(profession);
        }

        return "redirect:/admin/profession";
    }

    // Vacancy
    @GetMapping("/vacancy")
    public String vacancyPage(Model model) {
        model.addAttribute("vacancies", vacancyService.getVacancies());
        return "admin/vacancy";
    }

    @GetMapping("/vacancy/fill/db")
    public String fillDBWithVacancies(){
        parseManager.fillVacancy();
        return "redirect:/admin/vacancy";
    }

}
