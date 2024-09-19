package com.osipov.jobparser.controllers;

import com.osipov.jobparser.models.User;
import com.osipov.jobparser.services.UserService;
import com.osipov.jobparser.services.VacancyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account")
public class AccountController {
    private UserService userService;
    private VacancyService vacancyService;

    @Autowired
    public AccountController(UserService userService, VacancyService vacancyService) {
        this.userService = userService;
        this.vacancyService = vacancyService;
    }

    @GetMapping
    public String viewAccount(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("favorites", userService.findVacanciesOfUser(currentUser));
        model.addAttribute("username", currentUser.getUsername());

        return "auth/account";
    }

    @GetMapping("/remove/{vacancyId}")
    public String removeFromFavorite(@AuthenticationPrincipal User currentUser,
                                     @PathVariable("vacancyId") Long vacancyId, RedirectAttributes redirectAttributes) {
        userService.removeFavoriteVacancyFromUser(currentUser, vacancyId);

        return "redirect:/account";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login?logout";
    }
}
