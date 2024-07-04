package com.osipov.jobparser.controllers;

import com.osipov.jobparser.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping
    public String getMainPage(Model model){
        model.addAttribute("isAuthenticated", UserService.isUserAuthenticated());
        return "main";
    }
}
