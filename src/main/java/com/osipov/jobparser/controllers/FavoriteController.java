package com.osipov.jobparser.controllers;

import com.osipov.jobparser.models.User;
import com.osipov.jobparser.services.UserService;
import com.osipov.jobparser.services.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    private final UserService userService;

    @Autowired
    public FavoriteController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/add/{vacancyId}")
    public ResponseEntity<String> addToFavorites(@AuthenticationPrincipal User currentUser,
                                                 @PathVariable("vacancyId") Long vacancyId, RedirectAttributes redirectAttributes) {
        boolean added = userService.addFavoriteVacancyToUser(currentUser, vacancyId);
        if (added) {
            return ResponseEntity.ok("Vacancy added to favorites");
        } else {
            System.out.printf("vacancy_id : %s не добавилось к user_id : %s%n",
                    vacancyId, currentUser.getId());
            return ResponseEntity.badRequest().body("Vacancy already in favorites");
        }
    }

    @GetMapping("/remove/{vacancyId}")
    public ResponseEntity<String> removeFromFavorite(@AuthenticationPrincipal User currentUser,
                                                     @PathVariable("vacancyId") Long vacancyId, RedirectAttributes redirectAttributes) {
        boolean added = userService.removeFavoriteVacancyFromUser(currentUser, vacancyId);
        if (added) {
            return ResponseEntity.ok("Vacancy removed from favorites");
        } else {
            System.out.printf("vacancy_id : %s не удалилась из user_id : %s%n",
                    vacancyId, currentUser.getId());
            return ResponseEntity.badRequest().body("There was no vacancy in the favorites");
        }
    }
}
