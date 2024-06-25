package com.osipov.jobparser;

import com.osipov.jobparser.parsers.HHParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;

@SpringBootApplication
public class JobParserApplication {

	public static void main(String[] args) throws IOException {
		Dotenv dotenv = Dotenv.configure().load();
		System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
		System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
		System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

		SpringApplication.run(JobParserApplication.class, args);

		HHParser parser = new HHParser();
		for ( String s : parser.parse("https://hh.ru/search/vacancy?text=Программист")){
			System.out.println(s);
		}
	}

}
