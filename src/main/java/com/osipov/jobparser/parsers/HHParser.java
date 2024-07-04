package com.osipov.jobparser.parsers;

import com.osipov.jobparser.models.City;
import com.osipov.jobparser.models.Profession;
import com.osipov.jobparser.models.Skill;
import com.osipov.jobparser.models.Vacancy;
import org.springframework.stereotype.Component;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

@Component
public class HHParser extends Parser {

    public HHParser() {
        super.address = "https://hh.ru/search/vacancy?text=%s";
    }

    @Override
    public List<Vacancy> parse(Profession profession) throws IOException {
        String url = String.format(address, profession.getName().replaceAll("\\s+", "+"));

        Document document = getHtmlCode(url);
        Elements elements = document.select("div.vacancy-search-item__card.serp-item_link.vacancy-card-container--OwxCdOj5QlSlCBZvSggS");
        List<Vacancy> vacancies = new ArrayList<>();

        for (Element element : elements) {
            String individualUrl = element
                    .select("span.serp-item__title-link-wrapper")
                    .select("a.bloko-link").attr("href");

            if (individualUrl.length() > 255) continue;

            Document currentVacancy;
            try {
                currentVacancy = getHtmlCode(individualUrl);
            } catch (IOException e) {
                continue;
            }

            Vacancy vacancy = new Vacancy();
            String wage = checkElement(element
                    .select("span" +
                            ".fake-magritte-primary-text--Hdw8FvkOzzOcoR4xXWni" +
                            ".compensation-text--kTJ0_rp54B2vNeZ3CTt2" +
                            ".separate-line-on-xs--mtby5gO4J0ixtqzW38wh"));
            String company = checkElement(element
                    .select("span.company-info-text--vgvZouLtf8jwBmaD1xgp"));

            String title = currentVacancy
                    .select("div.vacancy-title").select("h1").text();

            if (title.isEmpty()) title = profession.getName();

            Elements skillsFromPage = currentVacancy
                    .select("ul.vacancy-skill-list--COfJZoDl6Y8AwbMFAh5Z > li");

            for (Element pageSkill : skillsFromPage) {
                String skillName = pageSkill.text();
                Skill skill = new Skill(skillName);
                vacancy.addSkill(skill);
            }

            City city = new City(checkElement(element
                    .select("span[data-qa=vacancy-serp__vacancy-address_narrow].bloko-text")
                    .select("span.fake-magritte-primary-text--Hdw8FvkOzzOcoR4xXWni")).strip().split("\\s+")[0]
            );

            if (!validateParameters(url, individualUrl, city.getName(), company)) continue;


            vacancy.setUrl(individualUrl);
            vacancy.setCity(city);
            vacancy.setCompany(company);
            vacancy.setWage(wage);
            vacancy.setProfession(profession);
            vacancy.setTitle(title);

            System.out.println(vacancy);
            vacancies.add(vacancy);
        }
        return vacancies;
    }

    private String checkElement(Element element) {
        if (element == null) return "";
        return element.text();
    }

    private String checkElement(Elements elements) {
        return checkElement(elements.first());
    }
}
