package com.osipov.jobparser.parsers;

import com.osipov.jobparser.models.Skill;
import com.osipov.jobparser.models.Vacancy;
import org.springframework.stereotype.Component;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

@Component
public class HHParser extends Parser{

    @Override
    public List<Vacancy> parse(String url) throws IOException {
        Document document = getHtmlCode(url);
        Elements elements = document.select("span.serp-item__title-link-wrapper > a.bloko-link");
        List<Vacancy> vacancies = new ArrayList<>();

        for (Element element : elements){
            String individualUrl = element.attr("href");
            Document currentVacancy = getHtmlCode(individualUrl);

            String address = currentVacancy.select("span.magritte-text___pbpft_3-0-8 > " +
                    "span.magritte-text___tkzIl_4-1-2").text();

            String city = address.split(",")[0].split("\\s+")[0];
            if (city.equals("Switch")) continue;

            String company = Objects.requireNonNull(
                    currentVacancy.select(
                            "a.bloko-link.bloko-link_kind-tertiary > " +
                                    "span.bloko-header-section-2.bloko-header-section-2_lite")
                            .select("span").first()).text();

            String title = currentVacancy.select("div.vacancy-title > h1").text();

            Elements elements1 = currentVacancy.select("ul.vacancy-skill-list--COfJZoDl6Y8AwbMFAh5Z > li");
            Set<Skill> skills = new HashSet<>();

            for (Element element1 : elements1){
                Skill skill = new Skill();
                skill.setName(element1.text());
                skills.add(skill);
            }

            if (!validateParameters(individualUrl, city, company, title)) continue;

            Vacancy vacancy = new Vacancy();
            vacancy.setUrl(individualUrl);
            vacancy.setCity(city);
            vacancy.setCompany(company);
            vacancy.setTitle(title);
            vacancy.setSkills(skills);
            System.out.println(
                    "url=" + vacancy.getUrl() +
                    ", city=" + city +
                    ", company=" + company +
                    ", title=" + title +
                    ", skills=" + skills);

            vacancies.add(vacancy);
        }
        return vacancies;
    }
}
