package com.osipov.jobparser;

import com.osipov.jobparser.managers.ParseManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;

@SpringBootApplication
public class JobParserApplication implements CommandLineRunner {
    @Autowired
    private ParseManager parseManager;

    public static void main(String[] args) throws IOException {
        Dotenv dotenv = Dotenv.configure().load();
        System.setProperty("USER_AGENT", dotenv.get("USER_AGENT"));
        System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
        System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
        System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

        SpringApplication.run(JobParserApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
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
