package com.osipov.jobparser.initializer;

import com.osipov.jobparser.models.Profession;
import com.osipov.jobparser.models.Role;
import com.osipov.jobparser.repositories.ProfessionRepository;
import com.osipov.jobparser.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private ProfessionRepository professionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (professionRepository.count() == 0) {
            professionRepository.save(new Profession("Программист"));
            professionRepository.save(new Profession("Аналитик"));
            professionRepository.save(new Profession("DevOps-инженер"));
            professionRepository.save(new Profession("UX/UI дизайнер"));
            professionRepository.save(new Profession("Руководитель проектов"));
            professionRepository.save(new Profession("Тестировщик"));
            professionRepository.save(new Profession("Системный инженер"));
            professionRepository.save(new Profession("Системный администратор"));
            professionRepository.save(new Profession("Системный аналитик"));
            professionRepository.save(new Profession("Веб-разработчик"));
            professionRepository.save(new Profession("Мобильный разработчик"));
            professionRepository.save(new Profession("Администратор баз данных"));
            professionRepository.save(new Profession("Инженер по безопасности"));
            professionRepository.save(new Profession("Инженер по машинному обучению"));
        }

        if (roleRepository.count() < 2) {
            roleRepository.save(new Role(1L, "ROLE_USER"));
            roleRepository.save(new Role(2L, "ROLE_ADMIN"));
        }
    }
}

