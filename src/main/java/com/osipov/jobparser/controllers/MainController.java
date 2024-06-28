package com.osipov.jobparser.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping
    public String getMain(Model model) {
        Path path = Paths.get("welcome.txt");
        String welcomeMessage = "";
        try {
            welcomeMessage = Files.readString(path);
        } catch (IOException e) {
            System.out.println("Файл jobParser/welcome.txt не найден");
        }
        model.addAttribute("welcomeMessage", welcomeMessage);
        return "main";
    }
}
