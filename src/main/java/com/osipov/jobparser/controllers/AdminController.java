package com.osipov.jobparser.controllers;

import com.osipov.jobparser.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        return "admin/admin";
    }

    @GetMapping("/{userID}/delete")
    public String deleteUser(@PathVariable("userID") Long userID, Model model) {
        userService.deleteUser(userID);
        return "redirect:/admin";
    }

    @GetMapping("/get/{userId}")
    public String getUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("allUsers", userService.usergtList(userId));
        return "admin/admin";
    }
}
