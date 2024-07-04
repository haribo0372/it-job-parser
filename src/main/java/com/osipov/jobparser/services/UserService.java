package com.osipov.jobparser.services;

import com.osipov.jobparser.models.Role;
import com.osipov.jobparser.models.User;
import com.osipov.jobparser.models.Vacancy;
import com.osipov.jobparser.repositories.RoleRepository;
import com.osipov.jobparser.repositories.UserRepository;
import com.osipov.jobparser.repositories.VacancyRepository;
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

        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean exist(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public void deleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getVacancies().clear();
            userRepository.save(user);
            userRepository.delete(user);
        }
    }

    public void createAdminUser(String username, String password) {
        if (userRepository.findByUsername(username) == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setPasswordConfirm(passwordEncoder.encode(password));

            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            if (adminRole == null) {
                adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }

            user.setRoles(Collections.singleton(adminRole));
            userRepository.save(user);
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
