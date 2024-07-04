package com.osipov.jobparser.controllers;

import com.osipov.jobparser.managers.ParseManager;
import com.osipov.jobparser.models.Profession;
import com.osipov.jobparser.repositories.ProfessionRepository;
import com.osipov.jobparser.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final ProfessionRepository professionRepository;
    private final ParseManager parseManager;

    @Autowired
    public AdminController(UserService userService, ProfessionRepository professionRepository, ParseManager parseManager) {
        this.userService = userService;
        this.professionRepository = professionRepository;
        this.parseManager = parseManager;
    }

    @GetMapping
    public String userList() {
        return "admin/main_admin";
    }

    // User
    @GetMapping("/user")
    public String userPage(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        return "admin/user";
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
    public String insertProfession(@ModelAttribute("professionForm") Profession profession) {
        if (profession.getName() == null || profession.getName().isEmpty())
            return "redirect:/admin/profession";

        Optional<Profession> professionOptional = professionRepository.findByName(profession.getName());
        if (professionOptional.isEmpty()) professionRepository.save(profession);
        return "redirect:/admin/profession";
    }

    @GetMapping("/profession/delete/{id}")
    public String deleteProf(@PathVariable("id") Long id) {
        professionRepository.deleteById(id);
        return "admin/profession";
    }

    // Vacancy
    @GetMapping("/vacancy")
    public String vacancyPage(Model model) {
        return "admin/vacancy";
    }

    @GetMapping("/vacancy/fill/db")
    public String fillDBWithVacancies(){
        parseManager.fillVacancy();
        return "redirect:/admin/vacancy";
    }

}
