package com.osipov.jobparser.parsers;

import com.osipov.jobparser.models.City;
import com.osipov.jobparser.models.Profession;
import com.osipov.jobparser.models.Skill;
import com.osipov.jobparser.models.Vacancy;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class HabrCareerParser extends Parser {
    public HabrCareerParser() {
        super.address = "https://career.habr.com/vacancies?q=%s&type=all";
    }

    @Override
    public List<Vacancy> parse(Profession profession) throws IOException {
        String url = String.format(address, profession.getName().replaceAll("\\s+", "+"));
        Document document = getHtmlCode(url);
        Elements boxes = document.select("div.section-box");

        List<Vacancy> vacancies = new ArrayList<>();

        for (Element element : boxes) {
            Vacancy vacancy = new Vacancy();

            String company = element.select("div.vacancy-card__company-title").text();

            Elements titleEls = element.select("div.vacancy-card__title");
            String title = titleEls.text();
            if (title.isEmpty()) title = profession.getName();

            City city = new City(element
                    .select("div.vacancy-card__meta")
                    .select("span.preserve-line > a").text().split("\\s+")[0]);

            String tempUrl = titleEls.select("a").attr("href");
            if (tempUrl.isEmpty()) continue;

            String individualUrl = "https://career.habr.com/" + tempUrl;
            String wage = element.select("div.vacancy-card__salary").text();

            String[] skillsOfBox = element.select("div.vacancy-card__skills").text().split("•");

            for (String skillOfBox : skillsOfBox) {
                if (skillOfBox.isEmpty() || !validateSkill(skillOfBox)) continue;
                vacancy.addSkill(new Skill(skillOfBox.strip()));
            }

            if (!validateParameters(url, individualUrl, company, city.getName())) continue;

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

    private boolean validateSkill(String skillName) {
        Pattern pattern = Pattern.compile(".*[а-яА-ЯёЁ].*");
        return !pattern.matcher(skillName).matches();
    }

}
