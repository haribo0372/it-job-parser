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

    @Override
    public List<Vacancy> parse(Profession profession) throws IOException {
        String url = String.format("https://hh.ru/search/vacancy?text=%s",
                profession.getName().replaceAll("\\s+", "+"));

        Document document = getHtmlCode(url);
        Elements elements = document.select("div.vacancy-search-item__card.serp-item_link.vacancy-card-container--OwxCdOj5QlSlCBZvSggS");
        List<Vacancy> vacancies = new ArrayList<>();

        for (Element element : elements) {
            String individualUrl = element
                    .select("span.serp-item__title-link-wrapper")
                    .select("a.bloko-link").attr("href");

            if (individualUrl.length() > 255) continue;

            Document currentVacancy = getHtmlCode(individualUrl);
            Vacancy vacancy = new Vacancy();
            String wage = checkElement(element
                    .select("div.wide-container--lnYNwDTY2HXOzvtbTaHf")
                    .select("div.compensation-labels--uUto71l5gcnhU2I8TZmz")
                    .select("span.bloko-text")
                    .select("span" +
                            ".fake-magritte-primary-text--Hdw8FvkOzzOcoR4xXWni" +
                            ".compensation-text--kTJ0_rp54B2vNeZ3CTt2" +
                            ".separate-line-on-xs--mtby5gO4J0ixtqzW38wh"));
            String company = checkElement(element
                    .select("div.info-section--N695JG77kqwzxWAnSePt")
                    .select("span.separate-line-on-xs--mtby5gO4J0ixtqzW38wh")
                    .select("span.bloko-text")
                    .select("a[data-qa=vacancy-serp__vacancy-employer].bloko-link.bloko-link_kind-secondary")
                    .select("span.company-info-text--vgvZouLtf8jwBmaD1xgp"));

            String title = checkElement(currentVacancy.select("div.vacancy-title > h1"));

            Elements skillsFromPage = currentVacancy.select("ul.vacancy-skill-list--COfJZoDl6Y8AwbMFAh5Z > li");

            for (Element pageSkill : skillsFromPage) {
                String skillName = pageSkill.text();
                Skill skill = new Skill(skillName);
                vacancy.addSkill(skill);
            }

            City city = new City(checkElement(element
                    .select("div.narrow-container--lKMghVwoLUtnGdJIrpW4")
                    .select("span[data-qa=vacancy-serp__vacancy-address_narrow].bloko-text")
                    .select("span.fake-magritte-primary-text--Hdw8FvkOzzOcoR4xXWni")).split("\\s+")[0]
            );

            if (!validateParameters(url, individualUrl, city.getName(), company, title)) continue;

            vacancy.setUrl(individualUrl);
            vacancy.setCity(city);
            vacancy.setCompany(company);
            vacancy.setWage(wage);
            vacancy.setProfession(profession);
            vacancy.setTitle(title);

            System.out.println(
                    "url=" + individualUrl +
                            ", wage=" + wage +
                            ", city=" + city +
                            ", company=" + company +
                            ", title=" + title +
                            ", prof=" + profession +
                            ", skills=" + vacancy.getSkills());

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
