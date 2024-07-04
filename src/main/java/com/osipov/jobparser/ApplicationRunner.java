package com.osipov.jobparser;

import com.osipov.jobparser.managers.ParseManager;
import com.osipov.jobparser.services.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunner implements CommandLineRunner {
    @Autowired
    private ParseManager parseManager;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        Dotenv dotenv = Dotenv.configure().load();
        userService.createAdminUser(dotenv.get("ADMIN_LOGIN"), dotenv.get("ADMIN_PASSWORD"));

        for (String arg : args) {
            if (arg.startsWith("--fill_db=")) {
                String fill_db = arg.substring("--fill_db=".length()).toLowerCase();
                if (fill_db.equals("true")) {
                    parseManager.fillVacancy();
                }
            }
        }
    }
}
