package com.osipov.jobparser.services;

import com.osipov.jobparser.models.Role;
import com.osipov.jobparser.models.User;
import com.osipov.jobparser.models.Vacancy;
import com.osipov.jobparser.repositories.RoleRepository;
import com.osipov.jobparser.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private VacancyService vacancyService;


    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setRoles(Collections.singleton(roleRepository.findByName(user.getRoleName())));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean exist(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public void deleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(userRepository::delete);
    }

   public void createAdminUser(String username, String password) {
        if (userRepository.findByUsername(username) == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setPasswordConfirm(passwordEncoder.encode(password));
            user.setRoleName("ROLE_ADMIN");
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");

            user.setRoles(Collections.singleton(adminRole));
            userRepository.save(user);
        }
    }

    @Transactional
    public boolean addFavoriteVacancyToUser(User user, Long vacancyId) {
        Optional<Vacancy> optionalVacancy = vacancyService.findById(vacancyId);
        if (optionalVacancy.isPresent()) {
            Vacancy vacancy = optionalVacancy.get();
            Set<Vacancy> vacanciesOfUser = findVacanciesOfUser(user);
            if (!vacanciesOfUser.contains(vacancy)) {
                vacanciesOfUser.add(vacancy);
                try {
                    userRepository.save(user);
                    System.out.println("Vacancy_id=" + vacancyId + " added to User_id=" + user.getId());
                } catch (Exception e) {
                    System.err.printf("VacancyService::addFavoriteVacancyToUser(%s, %s) -> \n" +
                            "\tError saving user_vacancy : %s : %s%n", user.getId(), vacancyId, e.getMessage(), e);
                    return false;
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Transactional
    public boolean removeFavoriteVacancyFromUser(User user, Long vacancyId) {
        Optional<Vacancy> optionalVacancy = vacancyService.findById(vacancyId);
        if (optionalVacancy.isPresent()) {
            Vacancy vacancy = optionalVacancy.get();
            Set<Vacancy> vacanciesOfUser = findVacanciesOfUser(user);
            if (vacanciesOfUser.contains(vacancy)) {
                vacanciesOfUser.remove(vacancy);
                try {
                    userRepository.save(user);
                    System.out.println("Vacancy_id=" + vacancyId + " remove from to User_id=" + user.getId());
                } catch (Exception e) {
                    System.err.printf("VacancyService::removeFavoriteVacancyFromUser(%s, %s) -> \n" +
                            "\tError saving user_vacancy : %s : %s%n", user.getId(), vacancyId, e.getMessage(), e);
                    return false;
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return (User) authentication.getPrincipal();
    }

    @Transactional
    public Set<Vacancy> findVacanciesOfUser(User user) {
        User initUser = userRepository.findById(user.getId()).orElseThrow(() ->
                new RuntimeException("UserService::findVacanciesOfUser() -> User not found"));

        initUser.getVacancies().size();
        return initUser.getVacancies();
    }

    public boolean vacancyExistsInFavoritesOfUser(Vacancy vacancy) {
        User user = getCurrentUser();
        if (user == null) throw new RuntimeException("The user is not logged in to the current session");

        Set<Vacancy> vacancies = findVacanciesOfUser(user);
        return vacancies.contains(vacancy);

    }

    public static boolean isUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public UserDetailsService userDetailsService() {
        return userRepository::findByUsername;
    }
}
